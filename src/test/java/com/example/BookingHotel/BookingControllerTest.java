package com.example.BookingHotel;


import com.example.BookingHotel.exception.BusinessException;
import com.example.BookingHotel.repository.RoomInventoryRepository;
import com.example.BookingHotel.request.BookedRoomRequest;
import com.example.BookingHotel.service.IBookingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


@SpringBootTest
public class BookingControllerTest {

    @Autowired
    private IBookingService bookingService;

    @Autowired
    private RoomInventoryRepository roomInventoryRepository;

    @Test
    @DisplayName("Thực hiện test đồng thời nhiều luông cùng vào đặt phòng khi chỉ còn một phòng")
    void concurrentBookingTest() throws InterruptedException {
        int numberOfUsers = 10;
        Long roomId = 47L;
        //gọi ExecutorService
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfUsers);
        //bắt đầu
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfUsers);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // tạo 10 users chờ lệnh
        for (int i = 0; i < numberOfUsers; i++) {
            final long customerId = i + 1;
            executorService.submit(() -> {
                try {
                    // Cả 10 thread sẽ nghẽn lại ở đây cho tới khi latch.countDown() được gọi
                    latch.await();

                    BookedRoomRequest request = createMockRequest(customerId);
                    bookingService.saveBooking(roomId, request);

                    successCount.incrementAndGet();
                } catch (BusinessException e) {
                    failCount.incrementAndGet();
                } catch (Exception e) {
                    e.printStackTrace();
                    failCount.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            });
        }
    }

    private BookedRoomRequest createMockRequest(long customerId) {
    }
}


