package app.ticket;

import app.ticket.ticketing.TicketingService;
import app.ticket.ticketing.db.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class ErrorExceptionTests {

    @Autowired
    private TicketingService ticketingService;
    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    void deleteAllData() {
        // 테스트 시작 전 데이터 전부 삭제
        ticketRepository.deleteAll();
    }

}

