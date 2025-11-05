package app.ticket.ticketing.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(value = {CustomException.class,  RuntimeException.class})
    public ResponseEntity<Map<String, String>> customExceptionHandler(CustomException e, HttpServletRequest request) {
        log.error("ExceptionHandler() 호출 - {}, {}", request.getRequestURI(), e.getErrorMessage());

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("code", String.valueOf(e.getErrorCode()));
        responseMap.put("message", String.valueOf(e.getErrorMessage()));

        return ResponseEntity.status(e.getHttpStatus()).body(responseMap);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("ExceptionHandler() 호출 - {}, {}", request.getRequestURI(), "올바르지 않은 Parameter를 요청했습니다.");

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("code", String.valueOf("4000"));
        responseMap.put("message", String.valueOf("올바르지 않은 Parameter를 요청했습니다."));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
    }
}
