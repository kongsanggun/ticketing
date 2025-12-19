package app.ticket.ticketing.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ErrorResponseDto> customExceptionHandler(
            CustomException e,
            HttpServletRequest request
    ) {
        log.error("ExceptionHandler() 호출 - {}, {}", request.getRequestURI(), e.getErrorMessage());
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorResponseDto(e.getMessage(), e.getDetail()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptionHandler(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        FieldError error = e.getBindingResult().getFieldErrors().get(0);
        String detail = error.getField() + " - " + error.getDefaultMessage();
        log.error("ExceptionHandler() 호출 - {}, {}", request.getRequestURI(), "올바르지 않은 Parameter를 요청했습니다.");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto("올바르지 않은 Parameter를 요청했습니다.", detail));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptionHandler(
            HttpMessageNotReadableException e,
            HttpServletRequest request
    ) {
        log.error("ExceptionHandler() 호출 - {}, {}", request.getRequestURI(), "올바르지 않은 Parameter를 요청했습니다.");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto("JSON 형식이 올바르지 않은 Parameter를 요청했습니다.", e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptionHandler(
            RuntimeException e,
            HttpServletRequest request
    ) {
        log.error("ExceptionHandler() 호출 - {}, {}", request.getRequestURI(), "서버 오류가 발생했습니다.");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto("서버 오류가 발생했습니다.", e.getMessage()));
    }
}
