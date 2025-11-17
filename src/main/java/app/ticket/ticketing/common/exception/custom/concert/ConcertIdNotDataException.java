package app.ticket.ticketing.common.exception.custom.concert;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;

public class ConcertIdNotDataException extends CustomException {
    public ConcertIdNotDataException() {
        super(ExceptionCode.NOT_DATA);
    }
}
