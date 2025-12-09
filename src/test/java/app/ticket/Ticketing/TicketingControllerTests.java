package app.ticket.Ticketing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import app.ticket.StartApplication;
import app.ticket.ticketing.db.Ticket;
import app.ticket.ticketing.ticketing.TicketRepository;
import app.ticket.ticketing.ticketing.TicketingController;
import app.ticket.ticketing.ticketing.TicketingRequestDto;
import app.ticket.ticketing.ticketing.TicketingResponseDto;
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
public class TicketingControllerTests extends TicketingTest {

    /*
     * TicketingController를 Test한다.
     */

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketingController ticketingController;

    private Ticket ticket;

    TicketingRequestDto setRequestData(String ticketId) {
        return new TicketingRequestDto(
                ticketId,
                concert.getConcertId(),
                stageList.get(0).getStageId(),
                seatClassList.get(0).getSeatClassId(),
                userList.get(0).getUserId()
        );
    }

    @BeforeEach()
    void setData() {
        setConcert();
        setStage(1);
        setSeatClass(1, new int[]{10000}, new int[]{2});
        setUserData(1);

        this.ticket = new Ticket(setRequestData(null), 1);
        ticketRepository.saveAndFlush(this.ticket);
    }

    @DisplayName("ticketing - 티켓팅 생성 컨트롤러 테스트 (지정 좌석)")
    @Test
    void createSeatedTicketControllerTest() {
        // given
        TicketingRequestDto dto = setRequestData(null);

        // when
        TicketingResponseDto result = ticketingController.createSeatedTicket(2, dto);

        // then
        assertThat(result.getTicketId().length(), is(13));
    }

    @DisplayName("ticketing - 티켓팅 생성 컨트롤러 테스트 (랜덤 좌석)")
    @Test
    void createRandomTicketControllerTest() {
        // given
        TicketingRequestDto dto = setRequestData(null);

        // when
        TicketingResponseDto result = ticketingController.createRandomTicket(dto);

        // then
        assertThat(result.getTicketId().length(), is(13));
    }

    @DisplayName("ticketing - 조회 컨트롤러 테스트")
    @Test
    void readTicketingControllerTest() {
        // when
        Ticket result = ticketingController.checkTicket(ticket.getTicketId());

        // then
        assertThat(result.getTicketId().length(), is(13));
        assertThat(result.getTicketId(), is(ticket.getTicketId()));
    }

    @DisplayName("ticketing - 삭제 컨트롤러 테스트")
    @Test
    void deleteTicketingControllerTest() {
        // given

        // when
        ticketingController.cancelTicket(setRequestData(ticket.getTicketId()));
        Ticket result = ticketRepository.findByTicketId(ticket.getTicketId());

        // then
        assertThat(result, is(nullValue()));
    }

    @AfterEach()
    void deleteData() {
        ticketRepository.deleteAll();
    }
}
