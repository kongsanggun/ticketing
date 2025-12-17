package app.ticket.Ticketing;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import app.ticket.StartApplication;
import app.ticket.ticketing.common.exception.custom.seatclass.SeatClassIdNotDataException;
import app.ticket.ticketing.common.exception.custom.ticket.TicketNotAvailableException;
import app.ticket.ticketing.common.exception.custom.ticket.TicketIdNotDataException;
import app.ticket.ticketing.common.exception.custom.ticket.TicketSelectedException;
import app.ticket.ticketing.common.exception.custom.user.NotEnoughPointsException;
import app.ticket.ticketing.common.exception.custom.user.NotExistedUserDataException;
import app.ticket.ticketing.db.Ticket;
import app.ticket.ticketing.ticketing.TicketRepository;
import app.ticket.ticketing.ticketing.TicketingRequestDto;
import app.ticket.ticketing.ticketing.TicketingResponseDto;
import app.ticket.ticketing.ticketing.TicketingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@Slf4j
@ContextConfiguration(classes = StartApplication.class)
public class TicketingServiceTests extends TicketingTest {

    /*
     * TicketingService를 Test한다.
     */

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketingService ticketingService;

    private Ticket ticket;

    TicketingRequestDto setRequestData(String ticketId, String seatClassId, String userId) {
        return new TicketingRequestDto(
                ticketId,
                concert.getConcertId(),
                stageList.get(0).getStageId(),
                seatClassId,
                userId
        );
    }

    @BeforeEach()
    void setData() {
        setConcert();
        setStage(1);
        setSeatClass(2, new int[]{10000, 50000}, new int[]{2, 2});
        setUserData(1);

        this.ticket = new Ticket(setRequestData(
                "test",
                seatClassList.get(0).getSeatClassId(),
                userList.get(0).getUserId()
        ), 1);
        ticketRepository.saveAndFlush(ticket);
    }

    @DisplayName("ticketing - 티켓팅 생성 서비스 테스트 (지정 좌석)")
    @Test
    void createSeatedTicketServiceTest() {
        // given
        TicketingRequestDto dto = setRequestData(
                null,
                seatClassList.get(0).getSeatClassId(),
                userList.get(0).getUserId()
        );

        // when
        TicketingResponseDto result = ticketingService.createSeatedTicket(2, dto);

        // then
        assertThat(result.getTicketId().length(), is(13));
        assertThatThrownBy(() -> ticketingService.createSeatedTicket(2, dto)).isInstanceOf(TicketSelectedException.class);

        assertThatThrownBy(() -> {
            TicketingRequestDto wrongDto = setRequestData(
                    null,
                    seatClassList.get(1).getSeatClassId(),
                    userList.get(0).getUserId()
            );
            ticketingService.createSeatedTicket(1, wrongDto);
        }).isInstanceOf(NotEnoughPointsException.class);

        assertThatThrownBy(() -> {
            TicketingRequestDto wrongDto = setRequestData(
                    null,
                    seatClassList.get(0).getSeatClassId(),
                    "wrongId"
            );
            ticketingService.createSeatedTicket(1, wrongDto);
        }).isInstanceOf(NotExistedUserDataException.class);

        assertThatThrownBy(() -> {
            TicketingRequestDto wrongDto = setRequestData(
                    null,
                    "wrongId",
                    userList.get(0).getUserId()
            );
            ticketingService.createSeatedTicket(1, wrongDto);
        }).isInstanceOf(SeatClassIdNotDataException.class);
    }

    @DisplayName("ticketing - 티켓팅 생성 서비스 테스트 (랜덤 좌석)")
    @Test
    void createRandomTicketServiceTest() {
        // given
        TicketingRequestDto dto = setRequestData(
                null,
                seatClassList.get(0).getSeatClassId(),
                userList.get(0).getUserId()
        );

        // when
        TicketingResponseDto result = ticketingService.createRandomTicket(dto);

        // then
        assertThat(result.getTicketId().length(), is(13));
        assertThatThrownBy(() -> {
            ticketingService.createRandomTicket(dto);
            ticketingService.createRandomTicket(dto);
        }).isInstanceOf(TicketNotAvailableException.class);
    }

    @DisplayName("ticketing - 조회 서비스 테스트")
    @Test
    void checkTicketServiceTest() {
        // when
        Ticket result = ticketingService.checkTicket(ticket.getTicketId());

        // then
        assertThat(result.getTicketId().length(), is(13));
        assertThat(result.getTicketId(), is(ticket.getTicketId()));
        assertThatThrownBy(() -> ticketingService.checkTicket("wrongTest"))
                        .isInstanceOf(TicketIdNotDataException.class);
    }

    @DisplayName("ticketing - 삭제 서비스 테스트")
    @Test
    void cancelTicketServiceTest() {
        // given
        TicketingRequestDto dto = setRequestData(
                ticket.getTicketId(),
                seatClassList.get(0).getSeatClassId(),
                userList.get(0).getUserId()
        );

        // when
        ticketingService.cancelTicket(dto);
        Ticket result = ticketRepository.findByTicketIdAndIsDelete(ticket.getTicketId(), false);

        // then
        assertThat(result, is(nullValue()));

        assertThatThrownBy(() -> {
            TicketingRequestDto wrongDto = setRequestData(
                    ticket.getTicketId(),
                    seatClassList.get(0).getSeatClassId(),
                    userList.get(0).getUserId()
            );
            ticketingService.cancelTicket(wrongDto);
        }).isInstanceOf(TicketIdNotDataException.class);

        assertThatThrownBy(() -> {
            TicketingRequestDto wrongDto = setRequestData(
                    null,
                    seatClassList.get(0).getSeatClassId(),
                    "wrongId"
            );
            ticketingService.cancelTicket(wrongDto);
        }).isInstanceOf(NotExistedUserDataException.class);

        assertThatThrownBy(() -> {
            TicketingRequestDto wrongDto = setRequestData(
                    null,
                    "wrongId",
                    userList.get(0).getUserId()
            );
            ticketingService.cancelTicket(wrongDto);
        }).isInstanceOf(SeatClassIdNotDataException.class);
    }

    @AfterEach()
    void deleteData() {
        ticketRepository.deleteAll();
    }
}
