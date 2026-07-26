package com.example.BookingHotel.exception;

import com.example.BookingHotel.constant.ErrorCode;
import com.example.BookingHotel.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class HotelGlobalExceptionHandler {

    //xử lý lỗi validate dữ liệu
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidation(MethodArgumentNotValidException ex) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    //xử lý lỗi nghiệp vụ tuỳ chỉnh
    @ExceptionHandler(BaseHotelException.class)
    public ResponseEntity<ApiResponse> handleHotelBusiness(BaseHotelException ex) {
        ErrorCode error = ex.getErrorCode();
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(error.getCode());
        apiResponse.setMessage(error.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    //lỗi hệ thống
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneralException(Exception ex) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    //xử lý lỗi không xác định(Internal Server Error)
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleSystemError(Exception ex) {
//        //log chi tiết, admin kiểm tra
//        ErrorResponse response = new ErrorResponse("INTERNAL_SERVER_ERROR", "Error system!" +
//                "please check again! ");
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
