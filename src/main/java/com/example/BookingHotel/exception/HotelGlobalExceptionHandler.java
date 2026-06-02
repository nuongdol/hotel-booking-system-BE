package com.example.BookingHotel.exception;

import com.example.BookingHotel.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class HotelGlobalExceptionHandler {

    //xử lý lỗi validate dữ liệu
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        ErrorResponse response = new ErrorResponse("VALIDATION_ERROR", "Data invalided!", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //xử lý lỗi nghiệp vụ tuỳ chỉnh
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleHotelBusiness(BaseHotelException ex) {
        ErrorResponse response = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(response, ex.getStatus());
    }

    //xử lý lỗi không xác định(Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleSystemError(Exception ex) {
        //log chi tiết, admin kiểm tra
        ErrorResponse response = new ErrorResponse("INTERNAL_SERVER_ERROR", "Error system!" +
                "please check again! ");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
