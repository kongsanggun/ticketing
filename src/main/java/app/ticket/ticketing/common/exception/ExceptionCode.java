package app.ticket.ticketing.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
    CHECKED_TICKET("4009", "이미 예약된 공연입니다.", HttpStatus.CONFLICT),
    SEAT_SELECTED("4003", "이미 선점된 자리입니다.", HttpStatus.FORBIDDEN),
    NOT_DATA("4001", "해당 값이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_INPUT("4001", "해당 입력값이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    ADDED_SHOW("4009", "이미 추가된 공연입니다.", HttpStatus.CONFLICT),
    EMPTY_PRICE("4022", "삭제 이후 공연 내 가격이 존재하지 않습니다.", HttpStatus.UNPROCESSABLE_ENTITY),
    EMPTY_STAGE("4022", "삭제 이후 공연 시간이 존재하지 않습니다. ", HttpStatus.UNPROCESSABLE_ENTITY),
    NOT_FOUND("4041", "Resource not found.", HttpStatus.NOT_FOUND),
    SERVER_ERROR("5001", "Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;         // 에러 코드
    private final String message;      // 에러 메시지
    private final HttpStatus httpStatus; // HTTP 상태 코드
}
