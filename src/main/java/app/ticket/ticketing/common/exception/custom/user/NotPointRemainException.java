package app.ticket.ticketing.common.exception.custom.user;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;

public class NotPointRemainException extends CustomException {
    public NotPointRemainException(String userId) {
        super(ExceptionCode.NOT_POINT, userId);
    }
}
