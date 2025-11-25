package app.ticket.ticketing.common.exception.custom.user;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;

public class UserNotDataException extends CustomException {
    public UserNotDataException(String userId) {
        super(ExceptionCode.NOT_DATA, userId);
    }
}
