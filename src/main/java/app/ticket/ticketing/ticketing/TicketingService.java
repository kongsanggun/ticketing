package app.ticket.ticketing.ticketing;

import app.ticket.ticketing.common.exception.custom.seatclass.SeatClassIdNotDataException;
import app.ticket.ticketing.common.exception.custom.ticket.TicketIdNotDataException;
import app.ticket.ticketing.common.exception.custom.ticket.TicketNotAvailableException;
import app.ticket.ticketing.common.exception.custom.ticket.TicketSelectedException;
import app.ticket.ticketing.common.exception.custom.user.NotEnoughPointsException;
import app.ticket.ticketing.common.exception.custom.user.NotExistedUserDataException;
import app.ticket.ticketing.db.SeatClass;
import app.ticket.ticketing.db.Ticket;
import app.ticket.ticketing.db.User;
import app.ticket.ticketing.redis.RedisLock;
import app.ticket.ticketing.seatclass.SeatClassRepository;
import app.ticket.ticketing.user.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketingService {
    private final TicketRepository ticketRepository;
    private final SeatClassRepository seatClassRepository;
    private final UserRepository userRepository;
    private final RedisLock redisLock;

    /*
     * 티켓을 예약한다. (좌석 지징)
     */
    public TicketingResponseDto createSeatedTicket(int seat, TicketingRequestDto request) {
        Ticket ticket = new Ticket(seat, request);
        SeatClass seatClass = getSeatClass(ticket.getSeatClassId());
        User user = getUser(ticket.getUserId());

        redisLock.getLock(ticket.getConcertId(), () -> {
            saveTicket(ticket, user, seatClass.getPrice());
        });

        return new TicketingResponseDto(ticket);
    }

    /*
     * 티켓을 예약한다. (랜덤 지징)
     */
    public TicketingResponseDto createRandomTicket(TicketingRequestDto request) {
        Ticket ticket = new Ticket(request);
        SeatClass seatClass = getSeatClass(ticket.getSeatClassId());
        User user = getUser(ticket.getUserId());

        redisLock.getLock(ticket.getConcertId(), () -> {
            List<Integer> savedSeats = ticketRepository.findAvailableSeat(
                    ticket.getConcertId(),
                    ticket.getStageId(),
                    ticket.getSeatClassId());
            if(savedSeats.isEmpty()) {
                throw new TicketNotAvailableException(ticket.getConcertId());
            }
            int index = (int) Math.round(Math.random() * savedSeats.size());
            ticket.setSeat(savedSeats.get(index));
            saveTicket(ticket, user, seatClass.getPrice());
        });

        return new TicketingResponseDto(ticket);
    }

    /*
     * 티켓을 예약한다.
     */
    private void saveTicket(Ticket ticket, User user, int price) {
        if (price> user.getPoint()) {
            throw new NotEnoughPointsException(user.getUserId());
        }
        user.setPoint(user.getPoint() - price);
        userRepository.save(user);
        try {
            ticketRepository.saveAndFlush(ticket);
        } catch (DataIntegrityViolationException e) {
            throw new TicketSelectedException("" + ticket.getSeat());
        }
    }

    /*
     * 티켓을 취소한다.
     */
    public void cancelTicket(TicketingRequestDto request) {
        SeatClass seatClass = getSeatClass(request.getSeatClassId());
        User user = getUser(request.getUserId());

        redisLock.getLock(request.getConcertId(), () -> {
            ticketRepository.findByTicketIdForUpdate(request.getTicketId()).orElseThrow(() -> {
                throw new TicketIdNotDataException(request.getTicketId());
            });
            user.setPoint(user.getPoint() + seatClass.getPrice());
            userRepository.save(user);
            ticketRepository.deleteByTicketId(request.getTicketId());
        });
    }

    /*
     * 티켓을 조회한다.
     */
    public Ticket checkTicket(String ticketId) {
        Ticket ticket = ticketRepository.findByTicketId(ticketId);
        if (ticket == null) {
            throw new TicketIdNotDataException(ticketId);
        }
        return ticket;
    }

    /*
     * SeatClass을 조회한다.
     */
    public SeatClass getSeatClass(String seatClassId) {
        SeatClass result = seatClassRepository.findBySeatClassId(seatClassId);
        if (result == null) {
            throw new SeatClassIdNotDataException(seatClassId);
        }
        return result;
    }

    /*
     * User를 조회한다.
     */
    public User getUser(String userId) {
        User result = userRepository.findByUserId(userId);
        if (result == null) {
            throw new NotExistedUserDataException(userId);
        }
        return result;
    }
}
