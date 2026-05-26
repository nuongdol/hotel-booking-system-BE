package com.example.BookingHotel.controller;

import com.example.BookingHotel.exception.InvalidPaymentRequestException;
import com.example.BookingHotel.model.BookedRoom;
import com.example.BookingHotel.model.Payment;
import com.example.BookingHotel.model.User;
import com.example.BookingHotel.response.PaymentResponse;
import com.example.BookingHotel.service.IPaymentService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/Payment")
@RequiredArgsConstructor
public class PaymentController {
    private final IPaymentService paymentService;

    //thanh toan cho dat phong
    @PostMapping("/hotel/{bookingId}/payments")
    public ResponseEntity<?> PostNewPayment(@PathVariable("bookingId") Long bookingId,
                                            @RequestBody Payment payment){
        try{
           if(bookingId == payment.getBooking().getBookingId()){
               paymentService.postPayment(payment.getId(), payment.getBooking(), payment.getUserEmail(),
                       payment.getPaymentMethod(), payment.getPaymentAccount(), payment.getPaymentStatus()
               ,payment.getTimestamp());
               return ResponseEntity.ok("Pay successfully");
           }
        }catch (InvalidPaymentRequestException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return null;
    }

    //lay thong tin thanh toan cu the
    @GetMapping("/hotel/payments/{id}")
    public ResponseEntity<?> getPayment(@PathVariable("id") Long idPayment){
        Optional<Payment> payment = paymentService.getPayment(idPayment);
        PaymentResponse paymentResponse = new PaymentResponse(payment.get().getId(), payment.get().getBooking(),
                payment.get().getUserEmail(), payment.get().getPaymentMethod(),payment.get().getPaymentAccount(),
                payment.get().getPaymentStatus(),payment.get().getTimestamp());

        return ResponseEntity.ok(paymentResponse);
    }

    //cap nhật thông tin thanh toán
    @PutMapping("/hotel/payments/{id}/payed")
    public ResponseEntity<?> updatePayment(@PathVariable Long id, @RequestBody Payment payment){
        try{
            if(id == payment.getId()){
                paymentService.updatePayment(payment.getId(),payment.getUserEmail(),
                        payment.getPaymentMethod(), payment.getPaymentAccount(), payment.getPaymentStatus(), payment.getTimestamp());
                return ResponseEntity.ok("Update Pay successfully");
            }
        }catch(InvalidPaymentRequestException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());

        }
        return null;
    }

    //Huỷ thông tin thanh toán
    @DeleteMapping("/hotel/payments/{id}")
    public void cancelPayment(@PathVariable("id") Long id) {
        paymentService.deletePayment(id);
    }


}
