package app.ticket.ticketing.common.exception.custom.ticket;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;

public class TicketSelectedException extends CustomException {
    public TicketSelectedException() {
        super(ExceptionCode.SEAT_SELECTED);
    }
}
