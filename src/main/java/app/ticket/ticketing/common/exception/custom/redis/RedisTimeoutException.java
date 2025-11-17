package app.ticket.ticketing.common.exception.custom.redis;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;

public class RedisTimeoutException extends CustomException {
    public RedisTimeoutException() {
        super(ExceptionCode.LOCK_TIME_OUT);
    }
}
