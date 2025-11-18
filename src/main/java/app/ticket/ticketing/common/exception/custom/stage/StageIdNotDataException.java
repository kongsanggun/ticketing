package app.ticket.ticketing.common.exception.custom.stage;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;

public class StageIdNotDataException extends CustomException {
    public StageIdNotDataException(String stageId) {
        super(ExceptionCode.NOT_DATA, stageId);
    }
}
