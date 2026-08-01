package com.example.BookingHotel.service;

import com.example.BookingHotel.constant.ResponseCode;
import com.example.BookingHotel.exception.BusinessException;
import com.example.BookingHotel.exception.ResourceNotFoundException;
import com.example.BookingHotel.mapper.BookingMapper;
import com.example.BookingHotel.model.BookedRoom;
import com.example.BookingHotel.model.Room;
import com.example.BookingHotel.model.RoomInventory;
import com.example.BookingHotel.repository.BookingRepository;
import com.example.BookingHotel.repository.RoomInventoryRepository;
import com.example.BookingHotel.request.InitPaymentRequest;
import com.example.BookingHotel.response.BookingResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;
    private final IRoomService roomService;
    private final IRoomInventoryService roomInventoryService;
    private final RoomInventoryRepository roomInventoryRepository;
    private final RedissonClient redissonClient;
    private static final int MAX_ROOM_STOCK = 20; //so luong toi da cho moi loai phong
    private final PriceService priceService;
    private final IPaymentService paymentService;
    private final BookingMapper bookingMapper;

    @Override
    public List<BookedRoom> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<BookedRoom> getBookingsByUserEmail(String email) {
        return bookingRepository.findByGuestEmail(email);
    }

    @Override
    @Transactional
    public BookingResponse markBooked(Long bookingId) {
        final Optional<BookedRoom> bookingOtp = bookingRepository.findByBookingId(bookingId);
        if (bookingOtp.isEmpty()) {
            throw new BusinessException(ResponseCode.BOOKING_NOT_FOUND);
        }
        final BookedRoom booking = bookingOtp.get();
        booking.setStatus(4);
        bookingRepository.save(booking);
        return bookingMapper.toBookingResponse(booking);
    }

    @Override
    public String getBookingStatus(Long bookingId) {
        final Optional<BookedRoom> bookingOtp = bookingRepository.findByBookingId(bookingId);
        if (bookingOtp.isEmpty()) {
            throw new BusinessException(ResponseCode.BOOKING_NOT_FOUND);
        }
        BookingResponse booking = bookingMapper.toBookingResponse(bookingOtp.get());
        /*
        pending = 1, confirmed = 2, cancelled = 3, completed = 4,
        no_show = 5
        */
        Integer status = booking.getStatus();
        String statusResponse;
        switch (status) {
            case 1:
                statusResponse = "Pending";
                break;
            case 2:
                statusResponse = "Confirmed";
                break;
            case 3:
                statusResponse = "Cancelled";
                break;
            case 4:
                statusResponse = "Completed";
                break;
            default:
                statusResponse = "no_show";
        }
        return statusResponse;
    }

    public boolean holdRoom(Long roomId, LocalDate checkIn,
                            LocalDate checkOut, String bookingCode, Integer bookedRoom) {
        /*
        1. giữ phòng tạm thời cho nhiều ngày từ ngày checkIn đến ngày checkOut
         */
        // tạo danh sách ngày trong khoảng checkIn đến ngày checkOut của khách hàng
        List<LocalDate> stayDates = getListDays(checkIn, checkOut);

        String generalLockKey = String.format("lock:room:%d", roomId);
        RLock lock = redissonClient.getLock(generalLockKey);
        try {
            //giu lock 60s
            if (lock.tryLock(60, TimeUnit.SECONDS)) {
                //tao key cho tung ngay, 1 ngày trong chuỗi ngày đặt phòng mà hết phòng thì thất bại
                for (LocalDate date : stayDates) {
                    String holdKey = String.format("hold:room:%d:%s", roomId, date.toString());
                    //lay cac phong co cac key giong nhu holdKey da co san trong redis
                    RMapCache<String, String> holdRoomMap = redissonClient.getMapCache(holdKey);
                    //những phòng đã được giu trước đó trong redis rồi
                    int holdRoomRedis = holdRoomMap.size();
                    //currentRoom = bookedRoom inDB + holdRoom in Redis
                    if ((holdRoomRedis + bookedRoom) > MAX_ROOM_STOCK) {
                        return false;
                    }
                }
                //nếu tất cả các ngày đều còn trống thì tien hanh giu phong
                //hoan toan cac phong luu trong redis lan nay la chua co trong redis truoc do, hoac co roi nhung bị xoa
                //thi van la ko ton tai trong redis
                for (LocalDate date : stayDates) {
                    String holdKey = String.format("hold:room:%d:%s", roomId, date.toString());
                    //add key
                    RMapCache<String, String> holdMap = redissonClient.getMapCache(holdKey);
                    //add field and value
                    holdMap.put(bookingCode, "HOLDING", 10, TimeUnit.MINUTES);
                }
                //giu cho thanh cong
                return true;
            }
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(ResponseCode.ERROR_SYSTEM);
        } finally {
            //giai phong khoa ngan han
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private List<LocalDate> getListDays(LocalDate checkIn, LocalDate checkOut) {
        List<LocalDate> listStayedDate = new ArrayList<>();
        LocalDate currentDate = checkIn;
        while (currentDate.isBefore(checkOut)) {
            listStayedDate.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }
        return listStayedDate;
    }

    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    @Override
    public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(() ->
                new ResourceNotFoundException("No booking found with booking code: " + confirmationCode));
    }

    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
        LocalDate checkinDate = bookingRequest.getCheckInDate();
        LocalDate checkoutDate = bookingRequest.getCheckOutDate();
        validateRequest(bookingRequest);
        /* check available room from check-indate to check-outdate */
        RoomInventory roomInventory = roomInventoryRepository.findByAvailabilityRoom(roomId, checkinDate, checkoutDate);
        if (roomInventory.getStock() <= 0) {
            throw new BusinessException(ResponseCode.ROOM_EMPTY_INVALID);
        }
        /* holding room for customer in 1 minute */
        //tạo một mã code cho giữ chỗ cho khach hang
        String bookingCode = UUID.randomUUID().toString();
        boolean holdRoomSuccess = holdRoom(roomId, bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate(), bookingCode, roomInventory.getBookedRoom());
        if (!holdRoomSuccess) {
            throw new BusinessException(ResponseCode.BOOKED_ROOM_INVALID);
        }
        /* payment */
        BigDecimal totalPrice = priceService.calculate(roomId, checkinDate, checkoutDate);
        bookingRequest.setFinalPrice(totalPrice);
        /* Optimistic lock + saveBooking in DB */
        List<LocalDate> lstStayedDay = getListDays(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());
        boolean completeBookingRoom = completeBookingPayment(roomId, lstStayedDay, bookingCode, bookingRequest);
        if (!completeBookingRoom) {
            throw new BusinessException(ResponseCode.BOOKED_ROOM_INVALID);
        }
        return bookingRequest.getBookingConfirmationCode();
    }

    private void validateRequest(BookedRoom bookingRequest) {
        /* check-indate > check-outdate*/
        LocalDate checkinDate = bookingRequest.getCheckInDate();
        LocalDate checkoutDate = bookingRequest.getCheckOutDate();
        LocalDate currentDate = LocalDate.now();
        if (checkinDate.isAfter(checkoutDate)) {
            throw new BusinessException(ResponseCode.CHECKIN_DATE_INVALID);
        }
        if (checkinDate.isBefore(currentDate) || checkoutDate.isBefore(currentDate)) {
            throw new BusinessException(ResponseCode.CHECKIN_DATE_INVALID);
        }
        if (bookingRequest.getTotalNumOfGuest() <= 0) {
            throw new BusinessException(ResponseCode.TOTAL_NUMBER_GUEST_INVALID);
        }
    }

    @Transactional
    public boolean completeBookingPayment(Long roomId, List<LocalDate> lstStayedDay,
                                          String bookingCode, BookedRoom bookingRequest) {
        try {

            Room room = roomService.getRoomById(roomId).get();
            int updateRows = 0;
            for (LocalDate date : lstStayedDay) {
                updateRows = roomInventoryService.processBookingRoom(roomId, date);
                //optimistic locking
                if (updateRows == 1) {//version trong database
                    room.addBooking(bookingRequest);
                    bookingRepository.save(bookingRequest);
                    finishedPayment(bookingRequest);
                } else {
                    throw new BusinessException(ResponseCode.CONFLICTED_ROOM);
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println("Failed Transaction: " + e.getMessage());
            return false;
        }
    }

    private void finishedPayment(BookedRoom bookingRequest) {
        var initPaymentRequest = InitPaymentRequest.builder()
                .customerId(bookingRequest.getCustomerId())
                .amount(bookingRequest.getFinalPrice().longValue())
                .txnRef(String.valueOf(bookingRequest.getBookingId()))
                .ipAddress(bookingRequest.getIpAddress())
                .build();
        var initPaymentResponse = paymentService.init(initPaymentRequest);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }
}
