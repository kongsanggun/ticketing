package app.ticket.ticketing.common.exception.custom.seatclass;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;

public class SeatClassIdNotDataException extends CustomException {
    public SeatClassIdNotDataException(String seatClassId) {
        super(ExceptionCode.NOT_DATA, seatClassId);
    }
}
