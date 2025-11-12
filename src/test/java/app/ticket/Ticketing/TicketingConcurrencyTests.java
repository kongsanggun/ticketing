package app.ticket.Ticketing;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.db.Ticket;
import app.ticket.ticketing.ticketing.TicketingRequestDto;
import app.ticket.ticketing.ticketing.TicketingResponseDto;
import app.ticket.ticketing.ticketing.TicketingService;
import app.ticket.ticketing.ticketing.TicketRepository;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@Slf4j
public class TicketingConcurrencyTests {
    @Autowired
    private TicketingService ticketingService;
    @Autowired
    private TicketRepository ticketRepository;

    private final ExecutorService executor = Executors.newFixedThreadPool(1000);

    private final AtomicInteger successCount = new AtomicInteger(0);
    private final AtomicInteger failCount = new AtomicInteger(0);

    private final CountDownLatch doneLatch = new CountDownLatch(1000);

    // 티켓을 예약해주는 스레드
    private void getTicket(int index, TicketingRequestDto dto) {
        executor.execute(() -> {
            try {
                TicketingResponseDto response = ticketingService.createTicket(dto);
                if(response != null) {
                    successCount.getAndIncrement();
                    log.info("Thread " + index + " - 완료 : " + dto.getSeat());
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
            } finally {
                doneLatch.countDown();
            }
        });
    }

    // 티켓을 취소해주는 스레드
    private void cancelTicket(int index) {
        executor.execute(() -> {
            try {
                List<Ticket> list = ticketRepository.findAll();
                if(!list.isEmpty()) {
                    TicketingRequestDto dto = new TicketingRequestDto(
                            list.get(0).getTicketId(), list.get(0).getUserId(), list.get(0).getShowId(), list.get(0).getSeat()
                    );
                    ticketingService.cancelTicket(dto);
                }
                successCount.getAndIncrement();
                log.info("Thread " + index + " - 예약이 취소되었습니다.");
            } catch (CustomException e) {
                successCount.getAndIncrement();
                log.info("Thread " + index + " - Exception : " + e.getErrorMessage());
            } catch (Exception e) {
                failCount.getAndIncrement();
                log.warn("Thread " + index + " - Exception : " + e.getMessage());
            } finally {
                doneLatch.countDown();
            }
        });
    }

    TicketingRequestDto setRequestData(String user, String seat) {
        TicketingRequestDto request = new TicketingRequestDto();

        request.setTicketId(UUID.randomUUID().toString().substring(0, 13));
        request.setUserId(user);
        request.setShowId("test");
        request.setSeat(seat);

        return request;
    }

    @Test
    @DisplayName("ticket - 멀티 스레드 테스트1 - 스레드 1000개 자리는 하나로 고정하고 예약한다.")
    void multiThreadTest1() throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            int index = i + 1;
            getTicket(index, setRequestData("user_" + index, "A1"));
        }
        doneLatch.await();

        // then
        assertThat((int) doneLatch.getCount(), is(0));
        assertThat(String.valueOf(successCount), is("1000"));
        assertThat(String.valueOf(failCount), is("0"));
    }

    @Test
    @DisplayName("ticket - 멀티 스레드 테스트2 - 멀티 스레트 테스트 2 (1000개 중 900개는 예약이고 100개는 예약 취소가 진행되는 스레드이다. 단, 자리는 하나로 고정하고 예약한다.)")
    void multiThreadTest2() throws InterruptedException {
        List<Integer> cancelList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            cancelList.add(500 + (int) Math.round(Math.random() * 450));
        }

        for (int i = 0; i < 1000; i++) {
            int index = i + 1;
            if(cancelList.contains(i)) {
                cancelTicket(index);
            } else {
                getTicket(index, setRequestData("user_" + index, "A1"));
            }
        }
        doneLatch.await();

        // then
        assertThat((int) doneLatch.getCount(), is(0));
        assertThat(String.valueOf(successCount), is("1000"));
        assertThat(String.valueOf(failCount), is("0"));
    }

    @Test
    @DisplayName("ticket - 멀티 스레드 테스트3 - 스레드 1000개")
    void multiThreadTest3() throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            int index = i + 1;
            executor.execute(() -> {
                final String seatNumber = String.valueOf(Math.round((Math.random() * 40) + 1));
                final String seat = String.valueOf((char)(Math.round((Math.random() * 14) + 65))) + seatNumber;
                getTicket(index, setRequestData("user_" + index, seat));
            });
        }
        doneLatch.await();

        // then
        assertThat((int) doneLatch.getCount(), is(0));
        assertThat(String.valueOf(successCount), is("1000"));
        assertThat(String.valueOf(failCount), is("0"));
    }

    @Test
    @DisplayName("ticket - 멀티 스레드 테스트4 - 멀티 스레트 테스트 2 (1000개 중 900개는 예약이고 100개는 예약 취소가 진행되는 스레드이다.)")
    void multiThreadTest4() throws InterruptedException {
        List<Integer> cancelList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            cancelList.add(500 + (int) Math.round(Math.random() * 450));
        }

        for (int i = 0; i < 1000; i++) {
            int index = i + 1;
            if(cancelList.contains(i)) {
                cancelTicket(index);
            } else {
                final String seatNumber = String.valueOf(Math.round((Math.random() * 40) + 1));
                final String seat = String.valueOf((char)(Math.round((Math.random() * 14) + 65))) + seatNumber;
                getTicket(index, setRequestData("user_" + index, seat));
            }
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


