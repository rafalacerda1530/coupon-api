package com.onebrain.coupon_api.adapters.in.controller.handler;
import com.onebrain.coupon_api.adapters.in.to.response.ErrorResponse;
import com.onebrain.coupon_api.application.core.exception.CouponAlreadyDeletedException;
import com.onebrain.coupon_api.application.core.exception.DomainException;
import com.onebrain.coupon_api.application.core.exception.DuplicateCouponCodeException;
import com.onebrain.coupon_api.application.core.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(
            DomainException ex,
            WebRequest request) {
        HttpStatus status = resolveStatus(ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                status.value(),
                LocalDateTime.now(),
                request.getDescription(false)
        );
        return ResponseEntity.status(status).body(errorResponse);
    }

    private HttpStatus resolveStatus(DomainException ex) {
        if (ex instanceof NotFoundException) {
            return HttpStatus.NOT_FOUND;
        }
        if (ex instanceof DuplicateCouponCodeException || ex instanceof CouponAlreadyDeletedException){
            return HttpStatus.CONFLICT;
        }

        return HttpStatus.BAD_REQUEST;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        StringBuilder message = new StringBuilder("Validation failed: ");
        errors.forEach((field, msg) -> message.append(field).append(": ").append(msg).append("; "));
        ErrorResponse errorResponse = new ErrorResponse(
                message.toString(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            WebRequest request) {
        String message = "Malformed JSON request";
        if (ex.getCause() != null) {
            String causeMessage = ex.getCause().getMessage();
            if (causeMessage != null) {
                if (causeMessage.contains("Cannot deserialize")) {
                    message = "Invalid data type in request body";
                } else if (causeMessage.contains("LocalDateTime")) {
                    message = "Invalid date format. Expected: yyyy-MM-ddTHH:mm:ss";
                } else if (causeMessage.contains("BigDecimal")) {
                    message = "Invalid number format for discount value";
                }
            }
        }
        ErrorResponse errorResponse = new ErrorResponse(
                message,
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(
            NullPointerException ex,
            WebRequest request) {
        String fieldName = extractFieldNameFromStackTrace(ex);
        String message = fieldName != null 
            ? String.format("Required field '%s' is null. Please provide a valid value.", fieldName)
            : "Required field is null. Please check your request body.";
        ErrorResponse errorResponse = new ErrorResponse(
                message,
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    private String extractFieldNameFromStackTrace(NullPointerException ex) {
        StackTraceElement[] stackTrace = ex.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            String methodName = element.getMethodName();
            if (methodName.equals("validateCode")) {
                return "code";
            } else if (methodName.equals("validateDiscountValueIsLessThanMin")) {
                return "discountValue";
            } else if (methodName.equals("validateExpirationDateIsBeforeDateNow")) {
                return "expirationDate";
            } else if (methodName.equals("validateMaxDescription")) {
                return "description";
            }
        }
        String exceptionMessage = ex.getMessage();
        if (exceptionMessage != null) {
            if (exceptionMessage.contains("code")) return "code";
            if (exceptionMessage.contains("discount")) return "discountValue";
            if (exceptionMessage.contains("expiration")) return "expirationDate";
            if (exceptionMessage.contains("description")) return "description";
        }
        return null;
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                "Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
