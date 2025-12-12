package app.ticket.ticketing.common.exception.custom.user;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;

public class NotEnoughPointsException extends CustomException {
    public NotEnoughPointsException(String userId) {
        super(ExceptionCode.NOT_POINT, userId);
    }
}
