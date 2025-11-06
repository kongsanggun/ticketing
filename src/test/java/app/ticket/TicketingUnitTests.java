package app.ticket;

import app.ticket.ticketing.ticketing.TicketingRequestDto;
import app.ticket.ticketing.db.Ticket;
import app.ticket.ticketing.ticketing.TicketRepository;
import io.hypersistence.tsid.TSID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Slf4j
public class TicketingUnitTests {

    @Autowired
    private TicketRepository ticketRepository;

    private Ticket ticket;

    TicketingRequestDto setRequestData() {
        final String seatNumber = String.valueOf(Math.round((Math.random() * 40) + 1));
        final String seat = String.valueOf((char)(Math.round((Math.random() * 14) + 65))) + seatNumber;

        TicketingRequestDto request = new TicketingRequestDto();
        request.setTicketId(TSID.fast().toString());
        request.setUserId(UUID.randomUUID().toString().substring(0, 13));
        request.setShowId("test");
        request.setSeat(seat);

        return request;
    }

    @BeforeEach()
    void setData() {
        this.ticket = new Ticket(setRequestData());
        this.ticket.setCreatedAt(new Date());

        ticketRepository.saveAndFlush(ticket);
    }

    @DisplayName("ticket - 생성 테스트")
    @Test
    void createTicketTest() {
        // given
        Ticket newData = new Ticket(setRequestData());
        newData.setCreatedAt(new Date());

        // when
        Ticket result = ticketRepository.saveAndFlush(newData);

        // then
        assertThat(result.getTicketId().length(), is(13));
        assertThat(result.getUserId(), is(newData.getUserId()));

        ticketRepository.delete(newData);
    }

    @DisplayName("ticket - 잘못 된 값으로 생성된 테스트")
    @Test
    void createWrongTicketTest() {
        // given
        Ticket notCreatedAtData = new Ticket(setRequestData());
        Ticket notIdData = new Ticket();

        // when

        // then
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            ticketRepository.saveAndFlush(notCreatedAtData);
        });
        Assertions.assertThrows(JpaSystemException.class, () -> {
            ticketRepository.saveAndFlush(notIdData);
        });
    }

    @DisplayName("ticket - 단건 조회 테스트 (ticketId)")
    @Test
    void readTicketIdTest() {
        // given
        Ticket testData= this.ticket;

        // when
        Ticket result = ticketRepository.findByTicketId(testData.getTicketId());

        // then
        assertThat(result.getTicketId().length(), is(13));
        assertThat(result.getTicketId(), is(testData.getTicketId()));
    }

    @DisplayName("ticket - 단건 조회 테스트 (userId)")
    @Test
    void readUserIdTest() {
        // given
        Ticket testData= this.ticket;

        // when
        Ticket result = ticketRepository.findByUserIdAndShowId(testData.getUserId(), testData.getShowId());

        // then
        assertThat(result.getTicketId().length(), is(13));
        assertThat(result.getUserId(), is(testData.getUserId()));
        assertThat(result.getShowId(), is(testData.getShowId()));
    }

    @DisplayName("ticket - 존재하지 않는 티켓 조회 테스트")
    @Test
    void readWrongTicketTest() {
        // given
        Ticket testData = new Ticket();

        // when
        Ticket result = ticketRepository.findByTicketId(testData.getTicketId());

        // then
        Assertions.assertThrows(NullPointerException.class, () -> {
            assertThat(result.getTicketId().length(), is(13));
            assertThat(result.getTicketId(), is(testData.getTicketId()));
        });
    }

    @DisplayName("ticket - 삭제 테스트")
    @Test
    void deleteTicketTest() {
        // given
        Ticket testData = this.ticket;

        // when
        ticketRepository.delete(testData);
        Ticket result = ticketRepository.findByTicketId(testData.getTicketId());

        // then
        Assertions.assertThrows(NullPointerException.class, () -> {
            result.getTicketId();
        });
    }

    @AfterEach()
    void deleteData() {
        ticketRepository.delete(this.ticket);
    }

}
