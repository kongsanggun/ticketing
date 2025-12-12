package app.ticket.ticketing.common.exception.custom.ticket;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;

public class TicketSelectedException extends CustomException {
    public TicketSelectedException(String seat) {
        super(ExceptionCode.SEAT_SELECTED, seat);
    }
}
