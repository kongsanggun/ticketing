package ticket.test.ticketing.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
}
