package app.ticket.ticketing.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
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
    public ResponseEntity<Map<String, String>> customExceptionHandler(CustomException e, HttpServletRequest request) {
        log.error("ExceptionHandler() 호출 - {}, {}", request.getRequestURI(), e.getErrorMessage());

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("message", String.valueOf(e.getErrorMessage()));
        responseMap.put("detail", e.getDetail());

        return ResponseEntity.status(e.getHttpStatus()).body(responseMap);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptionHandler(MethodArgumentNotValidException e,
                    HttpServletRequest request) {
        log.error("ExceptionHandler() 호출 - {}, {}", request.getRequestURI(), "올바르지 않은 Parameter를 요청했습니다.");

        Map<String, String> responseMap = new HashMap<>();
        FieldError error = e.getBindingResult().getFieldErrors().get(0);
        String detail = error.getField() + " - " + error.getDefaultMessage();

        responseMap.put("message", String.valueOf("올바르지 않은 Parameter를 요청했습니다."));
        responseMap.put("detail", detail);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptionHandler(HttpMessageNotReadableException e,
            HttpServletRequest request) {
        log.error("ExceptionHandler() 호출 - {}, {}", request.getRequestURI(), "올바르지 않은 Parameter를 요청했습니다.");

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("message", String.valueOf("JSON 형식이 올바르지 않은 Parameter를 요청했습니다."));
        responseMap.put("detail", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptionHandler(RuntimeException e,
            HttpServletRequest request) {
        log.error("ExceptionHandler() 호출 - {}, {}", request.getRequestURI(), "서버 오류가 발생했습니다.");

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("message", String.valueOf("서버 오류가 발생했습니다.."));
        responseMap.put("detail", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
    }
}
