package app.ticket.ticketing.common.exception.custom.ticket;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;

public class TicketIdNotDataException extends CustomException {
    public TicketIdNotDataException(String ticketId) {
        super(ExceptionCode.NOT_DATA, ticketId);
    }
}
