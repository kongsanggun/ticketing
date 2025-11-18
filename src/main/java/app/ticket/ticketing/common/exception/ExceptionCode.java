package app.ticket.ticketing.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
    CHECKED_TICKET("이미 예약된 공연입니다.", HttpStatus.CONFLICT),
    SEAT_SELECTED("이미 선점된 자리입니다.", HttpStatus.FORBIDDEN),
    NOT_DATA("해당 값이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    ADDED_SHOW("이미 추가된 공연입니다.", HttpStatus.CONFLICT),
    EMPTY_PRICE("삭제 이후 공연 내 가격이 존재하지 않습니다.", HttpStatus.UNPROCESSABLE_ENTITY),
    EMPTY_STAGE("삭제 이후 공연 시간이 존재하지 않습니다. ", HttpStatus.UNPROCESSABLE_ENTITY),
    LOCK_TIME_OUT("다른 요청이 처리 중입니다.", HttpStatus.CONFLICT),
    INTERRUPTED("요청이 중단되었습니다.", HttpStatus.REQUEST_TIMEOUT);

    private final String message; // 에러 메시지
    private final HttpStatus httpStatus; // HTTP 상태 코드
}
