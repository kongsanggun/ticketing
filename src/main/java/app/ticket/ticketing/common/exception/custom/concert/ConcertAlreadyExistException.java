package app.ticket.ticketing.common.exception.custom.concert;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;

public class ConcertAlreadyExistException extends CustomException {
    public ConcertAlreadyExistException(String concertId) {
        super(ExceptionCode.ADDED_SHOW, concertId);
    }
}
