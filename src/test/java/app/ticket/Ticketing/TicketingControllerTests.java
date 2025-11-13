package app.ticket.Ticketing;

import app.ticket.StartApplication;
import app.ticket.ticketing.db.Ticket;
import app.ticket.ticketing.ticketing.TicketRepository;
import app.ticket.ticketing.ticketing.TicketingController;
import app.ticket.ticketing.ticketing.TicketingRequestDto;
import app.ticket.ticketing.ticketing.TicketingResponseDto;
import io.hypersistence.tsid.TSID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Slf4j
@ContextConfiguration(classes = StartApplication.class)
public class TicketingControllerTests {

    /*
        TicketingController를 Test한다.
     */

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketingController ticketingController;

    private Ticket ticket;

    TicketingRequestDto setRequestData() {
        final String seatNumber = String.valueOf(Math.round((Math.random() * 40) + 1));
        final String seat = String.valueOf((char)(Math.round((Math.random() * 14) + 65))) + seatNumber;

        TicketingRequestDto request = new TicketingRequestDto();
        request.setTicketId(TSID.fast().toString());
        request.setUserId(UUID.randomUUID().toString().substring(0, 13));
        request.setShowId("controller");
        request.setSeat(seat);

        return request;
    }

    TicketingRequestDto setRequestData(Ticket ticket) {
        TicketingRequestDto request = new TicketingRequestDto();

        request.setTicketId(ticket.getTicketId());
        request.setUserId(ticket.getUserId());
        request.setShowId(ticket.getShowId());
        request.setSeat(ticket.getSeat());

        return request;
    }

    @BeforeEach()
    void setData() {
        this.ticket = new Ticket(setRequestData());
        this.ticket.setCreatedAt(new Date());

        ticketRepository.saveAndFlush(ticket);
    }

    @DisplayName("ticketing - 생성 컨트롤러 테스트")
    @Test
    void createTicketingControllerTest() throws Exception {
        // given
        Ticket newData = new Ticket(setRequestData());

        // when
        TicketingResponseDto result = ticketingController.createTicket(setRequestData(newData));

        // then
        assertThat(result.getTicketId().length(), is(13));
        assertThat(result.getShowId(), is("controller"));
    }

    @DisplayName("ticketing - 조회 컨트롤러 테스트")
    @Test
    void readTicketingControllerTest() {
        // when
        Ticket result =  ticketingController.checkTicket(ticket.getTicketId());

        // then
        assertThat(result.getTicketId().length(), is(13));
        assertThat(result.getTicketId(), is(ticket.getTicketId()));
        assertThat(result.getShowId(), is("controller"));
    }

    @DisplayName("ticketing - 삭제 컨트롤러 테스트")
    @Test
    void deleteTicketingControllerTest() {
        // given

        // when
        ticketingController.cancelTicket(setRequestData(ticket));
        Ticket result = ticketRepository.findByTicketId(ticket.getTicketId());

        // then
        assertThat(result, is(nullValue()));
    }

    @AfterEach()
    void deleteData() {
        ticketRepository.deleteAll();
    }
}
