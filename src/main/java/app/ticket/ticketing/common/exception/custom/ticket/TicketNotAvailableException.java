package app.ticket.ticketing.common.exception.custom.ticket;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;

public class TicketNotAvailableException extends CustomException {
    public TicketNotAvailableException(String detail) {
        super(ExceptionCode.SEAT_NOT_AVAILABLE, detail);
    }
}
