package app.ticket.ticketing.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public abstract class CustomException extends RuntimeException {

    private final HttpStatus httpStatus; // Http 상태값
    private final String errorMessage; // 에러 메시지

    public CustomException(ExceptionCode errorCode) {
        this.httpStatus = errorCode.getHttpStatus();
        this.errorMessage = errorCode.getMessage();
    }
}
