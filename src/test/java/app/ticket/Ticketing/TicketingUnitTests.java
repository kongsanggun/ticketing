package app.ticket.Ticketing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import app.ticket.ticketing.db.Ticket;
import app.ticket.ticketing.ticketing.TicketRepository;
import app.ticket.ticketing.ticketing.TicketingRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;

@SpringBootTest
@Slf4j
public class TicketingUnitTests {

    @Autowired
    private TicketRepository ticketRepository;

    private Ticket ticket;

    TicketingRequestDto setRequestData(String ticketId) {
        return new TicketingRequestDto(
                ticketId,
                "test",
                "test",
                "test",
                "test"
        );
    }

    @BeforeEach()
    void setData() {
        this.ticket = new Ticket(setRequestData("test"), 1);
        ticketRepository.saveAndFlush(ticket);
    }

    @DisplayName("ticket - 생성 테스트")
    @Test
    void createTicketTest() {
        // given
        Ticket newData = new Ticket(setRequestData("test"), 2);

        // when
        Ticket result = ticketRepository.saveAndFlush(newData);

        // then
        assertThat(result.getTicketId().length(), is(13));
        assertThat(result.getUserId(), is(newData.getUserId()));
    }

    @DisplayName("ticket - 중복 된 값으로 생성된 테스트")
    @Test
    void createDuplicateTicketTest() {
        // given

        // when

        // then
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            ticketRepository.saveAndFlush(this.ticket);
        });
    }

    @DisplayName("ticket - 잘못 된 값으로 생성된 테스트")
    @Test
    void createWrongTicketTest() {
        // given
        Ticket notIdData = new Ticket();

        // when

        // then
        Assertions.assertThrows(JpaSystemException.class, () -> {
            ticketRepository.saveAndFlush(notIdData);
        });
    }

    @DisplayName("ticket - 단건 조회 테스트 (ticketId)")
    @Test
    void readTicketIdTest() {
        // given
        Ticket testData = this.ticket;

        // when
        Ticket result = ticketRepository.findByTicketId(testData.getTicketId());

        // then
        assertThat(result.getTicketId().length(), is(13));
        assertThat(result.getTicketId(), is(testData.getTicketId()));
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
        assertThat(result, is(nullValue()));
    }

    @AfterEach()
    void deleteData() {
        ticketRepository.deleteAll();
    }
}
