package app.ticket.ticketing.ticketing;

import app.ticket.ticketing.common.exception.custom.ticket.TicketAlreadyExistException;
import app.ticket.ticketing.common.exception.custom.ticket.TicketIdNotDataException;
import app.ticket.ticketing.common.exception.custom.ticket.TicketSelectedException;
import app.ticket.ticketing.db.Ticket;
import app.ticket.ticketing.redis.RedisLock;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketingService {
    private final TicketRepository ticketRepository;
    private final RedisLock redisLock;

    /*
     * 티켓을 예약한다.
     */
    public TicketingResponseDto createTicket(TicketingRequestDto request) {
        Ticket ticket = new Ticket(request);
        saveTicket(ticket);
        return new TicketingResponseDto(ticket);
    }

    private void checkDuplicateRequest(Ticket ticket) {
        Ticket duplicate = ticketRepository.findByUserIdAndShowId(ticket.getUserId(), ticket.getShowId());
        if (duplicate != null) {
            throw new TicketAlreadyExistException();
        }
    }

    private void saveTicket(Ticket ticket) {
        redisLock.getLock(ticket.getShowId(), () -> {
            checkDuplicateRequest(ticket);
            Optional<Ticket> savedSeat = ticketRepository.findByShowIdAndSeat(ticket.getShowId(), ticket.getSeat());
            if (savedSeat.isPresent()) {
                throw new TicketSelectedException();
            }
            ticket.setCreatedAt(new Date());
            ticketRepository.saveAndFlush(ticket);
        });
    }

    /*
     * 티켓을 취소한다.
     */
    public void cancelTicket(TicketingRequestDto request) {
        redisLock.getLock(request.getShowId(), () -> {
            ticketRepository.findByTicketIdForUpdate(request.getTicketId()).orElseThrow(() -> {
                throw new TicketIdNotDataException();
            });
            ticketRepository.deleteByTicketId(request.getTicketId());
        });
    }

    /*
     * 티켓을 조회한다.
     */
    public Ticket checkTicket(String ticketId) {
        Ticket ticket = ticketRepository.findByTicketId(ticketId);
        if (ticket == null) {
            throw new TicketIdNotDataException();
        }
        return ticket;
    }
}
