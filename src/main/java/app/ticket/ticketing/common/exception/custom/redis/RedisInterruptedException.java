package app.ticket.ticketing.common.exception.custom.redis;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;

public class RedisInterruptedException extends CustomException {
    public RedisInterruptedException() {
        super(ExceptionCode.INTERRUPTED);
    }
}
