package app.ticket.ticketing.common.exception.custom.ticket;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;

public class TicketAlreadyExistException extends CustomException {
    public TicketAlreadyExistException() {
        super(ExceptionCode.CHECKED_TICKET);
    }
}
