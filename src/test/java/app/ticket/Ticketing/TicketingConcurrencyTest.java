package app.ticket.Ticketing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.ticketing.TicketRepository;
import app.ticket.ticketing.ticketing.TicketingRequestDto;
import app.ticket.ticketing.ticketing.TicketingResponseDto;
import app.ticket.ticketing.ticketing.TicketingService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class TicketingConcurrencyTest extends TicketingTest {
    @Autowired
    private TicketingService ticketingService;
    @Autowired
    private TicketRepository ticketRepository;

    private final ExecutorService executor = Executors.newFixedThreadPool(1000);

    private final AtomicInteger successCount = new AtomicInteger(0);
    private final AtomicInteger failCount = new AtomicInteger(0);

    private final CountDownLatch doneLatch = new CountDownLatch(1000);

    @BeforeEach()
    void setBasicData() {
        setConcert();
        setStage(3);
        setSeatClass(3, new int[]{10000, 30000, 50000}, new int[]{70, 70, 70});
        setUserData(500);
    }

    // 티켓을 예약해주는 스레드
    private void getTicket(int index, TicketingRequestDto dto) {
        executor.execute(() -> {
            try {
                TicketingResponseDto response = null;
                int random = (int) (Math.random() * (1000));
                if (random % 2 == 0) {
                    response = ticketingService.createRandomTicket(dto);
                } else {
                    response = ticketingService.createSeatedTicket((int) (Math.random() * (70)), dto);
                }
                if (response != null) {
                    successCount.getAndIncrement();
                    log.info("Thread " + index + " - 완료 : " + response.getConcertId() + "-" + response.getStageId() + "-" + response.getSeatClassId() + "-" + response.getSeat());
                } else {
                    failCount.getAndIncrement();
                    log.warn("Thread " + index + " - 티켓 예약에 실패하였습니다.");
                }
            } catch (CustomException e) {
                successCount.getAndIncrement();
                log.info("Thread " + index + " - Exception : " + e.getErrorMessage());
            } catch (Exception e) {
                failCount.getAndIncrement();
                log.warn("Thread " + index + " - Exception : " + e.getMessage());
                log.info("헉");
            } finally {
                doneLatch.countDown();
            }
        });
    }

    TicketingRequestDto setRequestData() {
        int stageIndex = (int) (Math.random() * (stageList.size() - 1));
        int seatClassIndex = (int) (Math.random() * (seatClassList.size() - 1));
        int userIndex = (int) (Math.random() * (userList.size() - 1));

        return new TicketingRequestDto(
                "",
                concert.getConcertId(),
                stageList.get(stageIndex).getStageId(),
                seatClassList.get(seatClassIndex).getSeatClassId(),
                userList.get(userIndex).getUserId()
        );
    }

    @Test
    @DisplayName("ticket - 멀티 스레드 테스트")
    void multiThreadTest() throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            int index = i + 1;
            getTicket(index, setRequestData());
        }
        doneLatch.await();

        // then
        assertThat((int) doneLatch.getCount(), is(0));
        assertThat(String.valueOf(successCount), is("1000"));
        assertThat(String.valueOf(failCount), is("0"));
    }

    @AfterEach()
    void deleteData() {
        // 테스트 시작 후 관련된 데이터 전부 삭제
        ticketRepository.deleteAll();
    }
}
