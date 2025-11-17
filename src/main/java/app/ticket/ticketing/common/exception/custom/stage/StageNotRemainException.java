package app.ticket.ticketing.common.exception.custom.stage;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;

public class StageNotRemainException extends CustomException {
    public StageNotRemainException() {
        super(ExceptionCode.EMPTY_STAGE);
    }
}
