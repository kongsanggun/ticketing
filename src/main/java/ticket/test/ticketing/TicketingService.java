package ticket.test.ticketing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ticket.test.ticketing.common.exception.CustomException;
import ticket.test.ticketing.common.exception.ExceptionCode;
import ticket.test.ticketing.db.Ticket;
import ticket.test.ticketing.db.TicketRepository;

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
        Ticket ticket = new Ticket(request);
        RLock lock = redissonClient.getLock("showTicket_" + request.getShowId());
        try {
            if(lock.tryLock(10000, 3000, TimeUnit.MILLISECONDS)) {
                saveTicket(ticket);
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
        return new TicketingResponseDto(ticket);
    }

    private void saveTicket(Ticket ticket) {
        try {
            Ticket duplicate = ticketRepository.findByUserIdAndShowId(ticket.getUserId(), ticket.getShowId());
            if(duplicate != null) {
                throw new CustomException(ExceptionCode.CHECKED_TICKET);
            }

            Optional<Ticket> savedSeat = ticketRepository.findByShowIdAndSeat(ticket.getShowId(), ticket.getSeat());
            if(savedSeat.isPresent()) {
                throw new CustomException(ExceptionCode.SEAT_SELECTED);
            }
        } catch (CustomException e) {
            throw new RuntimeException(e);
        }

        ticketRepository.saveAndFlush(ticket);
    }

    /*
     *  티켓을 취소한다.
     */
    public void cancelTicket(TicketingRequestDto request) {
        RLock lock = redissonClient.getLock("cancelTicket_" + request.getTicketId());
        try {
            if(lock.tryLock(10000, 3000, TimeUnit.MILLISECONDS)) {
                ticketRepository.findByTicketIdForUpdate(request.getTicketId()).orElseThrow(() -> {
                    try {
                        throw new CustomException(ExceptionCode.NOT_DATA);
                    } catch (CustomException e) {
                        throw new RuntimeException(e);
                    }
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
    }

    /*
     *  티켓을 조회한다.
     */
    public Ticket checkTicket(String ticketId) {
        return ticketRepository.findByTicketId(ticketId);
    }
}
