package com.example.BookingHotel.exception;

import com.example.BookingHotel.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/*
bắt lỗi cho toàn bộ RestController
 */
@RestControllerAdvice
public class HotelGlobalExceptionHandler {

    //xử lý lỗi validate dữ liệu
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiResponse> handleValidation(MethodArgumentNotValidException ex) {
//        ApiResponse apiResponse = new ApiResponse<>();
//        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
//        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
//        return ResponseEntity.badRequest().body(apiResponse);
//    }
//
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserAlreadyExistException(UserAlreadyExistsException e){
        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .status("ERROR")
                .code(HttpStatus.CONFLICT.value())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
