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
import app.ticket.ticketing.seatclass.SeatClassRepository;
import app.ticket.ticketing.user.UserRepository;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketingService {
    private final TicketRepository ticketRepository;
    private final SeatClassRepository seatClassRepository;
    private final UserRepository userRepository;
    private final Random random = new Random();

    /*
     * 티켓을 예약한다. (좌석 지징)
     */
    public TicketingResponseDto createSeatedTicket(int seat, TicketingRequestDto request) {
        Ticket result = saveTicket(
                new Ticket(request, seat),
                getSeatClass(request.getSeatClassId()),
                getUser(request.getUserId())
        );
        return new TicketingResponseDto(result);
    }

    /*
     * 티켓을 예약한다. (랜덤 지징)
     */
    public TicketingResponseDto createRandomTicket(TicketingRequestDto request) {
        int seat = getRandomSeat(request);

        Ticket result = saveTicket(
                new Ticket(request, seat),
                getSeatClass(request.getSeatClassId()),
                getUser(request.getUserId())
        );
        return new TicketingResponseDto(result);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    private Ticket saveTicket(Ticket ticket, SeatClass seatClass, User user) {
        int result = userRepository.usePoint(user.getUserId(), seatClass.getPrice());
        if (result == 0) {
            throw new NotEnoughPointsException(user.getUserId());
        }
        try {
            return ticketRepository.saveAndFlush(ticket);
        } catch (DataIntegrityViolationException e) {
            throw new TicketSelectedException("" + ticket.getSeat());
        }
    }

    /*
     * 티켓을 취소한다.
     */
    @Transactional()
    public void cancelTicket(TicketingRequestDto request) {
        SeatClass seatClass = getSeatClass(request.getSeatClassId());
        User user = getUser(request.getUserId());
        Ticket ticket = ticketRepository.findByTicketIdAndIsDelete(request.getTicketId(), false);
        if (ticket == null) {
            throw new TicketIdNotDataException(request.getTicketId());
        }

        user.chargePoint(seatClass.getPrice());
        userRepository.save(user);

        ticket.deleteData();
        ticketRepository.save(ticket);
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
     * 랜덤 자리를 지정한다.
     */
    private int getRandomSeat(TicketingRequestDto dto) {
        List<Integer> savedSeats = ticketRepository.findAvailableSeat(
                dto.getConcertId(),
                dto.getStageId(),
                dto.getSeatClassId());
        if (savedSeats.isEmpty()) {
            throw new TicketNotAvailableException(dto.getConcertId());
        }
        int index = random.nextInt(savedSeats.size());
        return savedSeats.get(index);
    }

    /*
     * SeatClass을 조회한다.
     */
    private SeatClass getSeatClass(String seatClassId) {
        SeatClass result = seatClassRepository.findBySeatClassId(seatClassId);
        if (result == null) {
            throw new SeatClassIdNotDataException(seatClassId);
        }
        return result;
    }

    /*
     * User를 조회한다.
     */
    private User getUser(String userId) {
        User result = userRepository.findByUserId(userId);
        if (result == null) {
            throw new NotExistedUserDataException(userId);
        }
        return result;
    }
}
