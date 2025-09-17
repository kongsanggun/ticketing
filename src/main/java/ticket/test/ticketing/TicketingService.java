package ticket.test.ticketing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ticket.test.ticketing.db.Ticket;
import ticket.test.ticketing.db.TicketRepository;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional()
public class TicketingService {
    private final TicketRepository ticketRepository;

    private final RedissonClient redissonClient;

    /*
    *  티켓을 예약한다.
    */

    public TicketingResponseDto createTicket(TicketingRequestDto request) {
        TicketingResponseDto result;
        RLock lock = redissonClient.getLock("showTicket_" + request.getShowId());
        try {
            if(lock.tryLock(10000, 3000, TimeUnit.MILLISECONDS)) {
                result = saveTicket(request);
            } else {
                throw new RuntimeException("Unabled to acquire lock");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if(lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return result;
    }

    private TicketingResponseDto saveTicket(TicketingRequestDto request) {
        Ticket duplicate = ticketRepository.findByUserIdAndShowId(request.getUserId(), request.getShowId());
        if(duplicate != null) {
            return new TicketingResponseDto("이미 예약된 공연입니다.", null);
        }

        Optional<Ticket> savedSeat = ticketRepository.findByShowIdAndSeat(request.getShowId(), request.getSeat());
        if(savedSeat.isPresent()) {
            return new TicketingResponseDto("이미 선점된 자리입니다.", null);
        }

        Ticket ticket = new Ticket(
                request.getTicketId(), request.getUserId(), request.getShowId(), request.getSeat(), new Date()
        );
        ticketRepository.saveAndFlush(ticket);
        return new TicketingResponseDto("예약이 완료되었습니다.", ticket);
    }

    /*
     *  티켓을 취소한다.
     */
    @Transactional()
    public TicketingResponseDto cancelTicket(TicketingRequestDto request) {
        TicketingResponseDto result;
        RLock lock = redissonClient.getLock("cancelTicket_" + request.getTicketId());
        try {
            if(lock.tryLock(10000, 3000, TimeUnit.MILLISECONDS)) {
                ticketRepository.findByTicketIdForUpdate(request.getTicketId()).orElseThrow(() -> {
                    throw new IllegalArgumentException("해당 ID가 존재하지 않습니다.");
                });
                ticketRepository.deleteByTicketId(request.getTicketId());
            } else {
                throw new RuntimeException("Unabled to acquire lock");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if(lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        return new TicketingResponseDto("예약 취소가 완료되었습니다.", null);
    }

    /*
     *  티켓을 조회한다.
     */
    public Ticket checkTicket(String ticketId) {
        return ticketRepository.findByTicketId(ticketId);
    }
}
