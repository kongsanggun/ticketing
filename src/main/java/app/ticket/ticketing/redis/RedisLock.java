package app.ticket.ticketing.redis;

import app.ticket.ticketing.common.Logic;
import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisLock {
    private final RedissonClient redissonClient;

    /*
     * 분산 락(redis)을 불러온다.
     */
    public void getLock(String name, Logic logic) {
        RLock lock = redissonClient.getLock(name);
        try {
            if (lock.tryLock(10, 5, TimeUnit.SECONDS)) {
                logic.execute();
            } else {
                throw new CustomException(ExceptionCode.LOCK_TIME_OUT);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CustomException(ExceptionCode.INTERRUPTED);
        } finally {
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
