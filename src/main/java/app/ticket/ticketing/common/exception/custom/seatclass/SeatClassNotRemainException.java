package app.ticket.ticketing.common.exception.custom.seatclass;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;

public class SeatClassNotRemainException extends CustomException {
    public SeatClassNotRemainException() {
        super(ExceptionCode.EMPTY_PRICE);
    }
}
