package app.ticket.ticketing.ticketing;

import app.ticket.ticketing.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import app.ticket.ticketing.common.exception.ExceptionCode;
import app.ticket.ticketing.db.Ticket;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

interface ILogic {
    void execute();
}

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional()
public class TicketingService {
    private final TicketRepository ticketRepository;

    private final RedissonClient redissonClient;

    /*
     *  분산 락(redis)을 불러온다.
     */
    private void getLock(String name, ILogic logic) {
        RLock lock = redissonClient.getLock(name);
        try {
            if(lock.tryLock(10, 5, TimeUnit.SECONDS)) {
                logic.execute();
            } else {
                throw new CustomException(ExceptionCode.LOCK_TIME_OUT);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CustomException(ExceptionCode.INTERRUPTED);
        } finally {
            if(lock != null && lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    /*
    *  티켓을 예약한다.
    */
    public TicketingResponseDto createTicket(TicketingRequestDto request) {
        Ticket ticket = new Ticket(request);
        saveTicket(ticket);
        return new TicketingResponseDto(ticket);
    }

    private void checkDuplicateRequest(Ticket ticket) {
        Ticket duplicate = ticketRepository.findByUserIdAndShowId(ticket.getUserId(), ticket.getShowId());
        if(duplicate != null) {
            throw new CustomException(ExceptionCode.CHECKED_TICKET);
        }
    }

    private void saveTicket(Ticket ticket) {
        getLock(ticket.getShowId(), () -> {
            checkDuplicateRequest(ticket);
            Optional<Ticket> savedSeat = ticketRepository.findByShowIdAndSeat(ticket.getShowId(), ticket.getSeat());
            if(savedSeat.isPresent()) {
                throw new CustomException(ExceptionCode.SEAT_SELECTED);
            }
            ticket.setCreatedAt(new Date());
            ticketRepository.saveAndFlush(ticket);
        });
    }

    /*
     *  티켓을 취소한다.
     */
    public void cancelTicket(TicketingRequestDto request) {
        getLock(request.getShowId(), () -> {
            ticketRepository.findByTicketIdForUpdate(request.getTicketId()).orElseThrow(() -> {
                throw new CustomException(ExceptionCode.NOT_DATA);
            });
            ticketRepository.deleteByTicketId(request.getTicketId());
        });
    }

    /*
     *  티켓을 조회한다.
     */
    public Ticket checkTicket(String ticketId) {
        Ticket ticket =  ticketRepository.findByTicketId(ticketId);
        if (ticket == null) {
            throw new CustomException(ExceptionCode.NOT_DATA);
        }
        return ticket;
    }
}
