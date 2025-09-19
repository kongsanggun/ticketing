package ticket.test.ticketing.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
    CHECKED_TICKET("4001", "이미 예약된 공연입니다.", HttpStatus.BAD_REQUEST),
    SEAT_SELECTED("4003", "이미 선점된 자리입니다.", HttpStatus.FORBIDDEN),
    NOT_DATA("4001", "해당 값이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_INPUT("4001", "해당 입력값이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND("4041", "Resource not found.", HttpStatus.NOT_FOUND),
    SERVER_ERROR("5001", "Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;         // 에러 코드
    private final String message;      // 에러 메시지
    private final HttpStatus httpStatus; // HTTP 상태 코드
}
