package app.ticket.Ticketing;

import app.ticket.StartApplication;
import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;
import app.ticket.ticketing.db.Ticket;

import app.ticket.ticketing.ticketing.TicketRepository;
import app.ticket.ticketing.ticketing.TicketingRequestDto;
import app.ticket.ticketing.ticketing.TicketingResponseDto;
import app.ticket.ticketing.ticketing.TicketingService;
import io.hypersistence.tsid.TSID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Slf4j
@ContextConfiguration(classes = StartApplication.class)
public class TicketingServiceTests {

    /*
        TicketingService를 Test한다.
     */

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketingService ticketingService;

    private Ticket ticket;

    TicketingRequestDto setRequestData(String seat) {
        TicketingRequestDto request = new TicketingRequestDto();
        request.setTicketId(TSID.fast().toString());
        request.setUserId(UUID.randomUUID().toString().substring(0, 13));
        request.setShowId("service");
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
        this.ticket = new Ticket(setRequestData("A1"));
        ticketRepository.saveAndFlush(ticket);
    }

    @DisplayName("ticketing - 생성 서비스 테스트")
    @Test
    void createTicketServiceTest() {
        // given
        TicketingRequestDto dto = setRequestData("B1");

        // when
        TicketingResponseDto result = ticketingService.createTicket(dto);

        // then
        assertThat(result.getTicketId().length(), is(13));
        assertThat(result.getShowId(), is("service"));

        assertThatThrownBy(() -> ticketingService.createTicket(dto))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorMessage", ExceptionCode.CHECKED_TICKET.getMessage());
    }

    @DisplayName("ticketing - 조회 서비스 테스트")
    @Test
    void checkTicketServiceTest() {
        // when
        Ticket result = ticketingService.checkTicket(ticket.getTicketId());

        // then
        assertThat(result.getTicketId().length(), is(13));
        assertThat(result.getTicketId(), is(ticket.getTicketId()));
        assertThat(result.getShowId(), is("service"));

        assertThatThrownBy(() -> ticketingService.checkTicket("wrongTest"))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorMessage", ExceptionCode.NOT_DATA.getMessage());
    }

    @DisplayName("ticketing - 삭제 서비스 테스트")
    @Test
    void cancelTicketServiceTest() {
        // given

        // when
        ticketingService.cancelTicket(setRequestData(ticket));
        Ticket result = ticketRepository.findByTicketId(ticket.getTicketId());

        // then
        assertThat(result, is(nullValue()));

        assertThatThrownBy(() -> ticketingService.cancelTicket(setRequestData(ticket)))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorMessage", ExceptionCode.NOT_DATA.getMessage());
    }

    @AfterEach()
    void deleteData() {
        ticketRepository.deleteAll();
    }
}
