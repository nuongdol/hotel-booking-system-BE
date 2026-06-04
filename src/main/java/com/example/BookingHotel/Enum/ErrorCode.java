package com.example.BookingHotel.Enum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@NoArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error",HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002,"User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004,"Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005,"User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_AUTHORIZATION_HEADER(1009, "Invalid Authorization header", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED("Role not existed", HttpStatus.NOT_FOUND),
    SEND_EMAIL(1010, "The system doesn't send email", HttpStatus.BAD_REQUEST),
    OTP_NOT_EXISTED_OR_VERIFIED(1011,"Otp expired" ,HttpStatus.NOT_FOUND ),
    UNAUTHORIZED_REDIRECT_URI(1012, "Sorry! We've got an Unauthorized Redirect URI and can't proceed with " +
                                      "the authentication", HttpStatus.BAD_REQUEST),
    OAUTH2_AUTHENTICATION_PROCESSING(1013, "Sorry! Login google is not supported yet.",
                                     HttpStatus.BAD_REQUEST),
    EMAIL_OAUTH2_AUTHENTICATION_PROCESSING(1014, "Email not found from OAuth2 provider",
                                           HttpStatus.BAD_REQUEST),
    PAYMENT_PROCESSING(1015, "Payment method is not existed.", HttpStatus.BAD_REQUEST)
            ;
    private Integer code;
    private String message;
    private HttpStatusCode statusCode;

    ErrorCode(Integer code, String message, HttpStatusCode statusCode){
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
    ErrorCode(String message, HttpStatusCode statusCode){
        this.message = message;
        this.statusCode = statusCode;
    }
}
