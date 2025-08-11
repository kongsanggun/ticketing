package ticket.test.ticketing;

import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ticket.test.ticketing.db.Ticket;
import ticket.test.ticketing.db.TicketRepository;

import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class TicketingService {

    @Autowired
    private final TicketRepository ticketRepository;

    /*
    *  티켓을 예약한다.
    */
    @Transactional()
    public TicketingResponseDto getTicket(TicketingRequestDto request) {
        Ticket duplicate = ticketRepository.findByUserIdAndShowId(request.getUserId(), request.getShowId());
        if(duplicate != null) {
            return new TicketingResponseDto("예약이 완료되었습니다.", null);
        }

        Optional<Ticket> savedSeat = ticketRepository.findByShowIdAndSeatForUpdate(request.getShowId(), request.getSeat());
        if(savedSeat.isPresent()) {
            return new TicketingResponseDto("예약이 완료되었습니다.", null);
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
        ticketRepository.findByTicketIdForUpdate(request.getTicketId()).orElseThrow(() -> {
            throw new IllegalArgumentException("예약 취소가 완료되었습니다.");
        });

        ticketRepository.deleteByTicketId(request.getTicketId());
        return new TicketingResponseDto("예약 취소가 완료되었습니다.", null);
    }

    /*
     *  티켓을 조회한다.
     */
    public Ticket checkTicket(String ticketId) {
        return ticketRepository.findByTicketId(ticketId);
    }

}
