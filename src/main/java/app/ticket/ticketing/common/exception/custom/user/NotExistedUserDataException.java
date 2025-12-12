package app.ticket.ticketing.common.exception.custom.user;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;

public class NotExistedUserDataException extends CustomException {
    public NotExistedUserDataException(String userId) {
        super(ExceptionCode.NOT_DATA, userId);
    }
}
