package ticket.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ticket.test.ticketing.TicketingRequestDto;
import ticket.test.ticketing.TicketingService;
import ticket.test.ticketing.db.TicketRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Slf4j
class TestApplicationTests {

	@Autowired
	private TicketingService ticketingService;
	@Autowired
	private TicketRepository ticketRepository;

	@BeforeEach
	void deleteAllData() {
		// 테스트 시작 전 데이터 전부 삭제
		ticketRepository.deleteAll();
	}

	/*
	멀티 스레트 테스트 1 (스레드 10개 자리는 하나로 고정하고 예약한다.)
	*/
	@Test
	@DisplayName("예약 - 멀티 스레드 테스트 - 1")
	void multiThreadTest1() throws InterruptedException {
		AtomicInteger successCount = new AtomicInteger(0);
		ExecutorService executor = Executors.newFixedThreadPool(10);
		// 작업 스레드가 끝날 때 까지 기다리는 역할이다.
		CountDownLatch latch = new CountDownLatch(10);

		for (int i = 0; i < 10; i++) {
			int index = i + 1;
			executor.execute(() -> {
				try {
					final String uuid = UUID.randomUUID().toString();
					TicketingRequestDto dto = new TicketingRequestDto(uuid, "TEST", "TEST", "A1");

					TicketingRequestDto response = ticketingService.createTicket(dto);
					if(response != null) {
						successCount.getAndIncrement();
					}
					log.info("Thread " + index + " - " + "완료");
				} catch (Exception e) {
					log.error("[error] Thread " + index + " - " + e.getMessage());
				}
				latch.countDown();
			});
		}
		latch.await();

		assertThat(String.valueOf(successCount), is("1"));
	}

	/*
    멀티 스레트 테스트 2 (스레드 100개 자리는 하나로 고정하고 예약한다.)
 	*/
	@Test
	@DisplayName("예약 - 멀티 스레드 테스트 - 2")
	void multiThreadTest2() throws InterruptedException {
		AtomicInteger successCount = new AtomicInteger(0);
		ExecutorService executor = Executors.newFixedThreadPool(100);
		// 작업 스레드가 끝날 때 까지 기다리는 역할이다.
		CountDownLatch latch = new CountDownLatch(100);

		for (int i = 0; i < 100; i++) {
			int index = i + 1;
			executor.execute(() -> {
				try {
					final String uuid = UUID.randomUUID().toString();
					TicketingRequestDto dto = new TicketingRequestDto(uuid, "TEST", "TEST", "A1");

					TicketingRequestDto response = ticketingService.createTicket(dto);
					if(response != null) {
						successCount.getAndIncrement();
					}
					log.info("Thread " + index + " - 완료");
				} catch (Exception e) {
					log.error("[error] Thread " + index + " - " + e.getMessage());
				}
				latch.countDown();
			});
		}
		latch.await();

		assertThat(String.valueOf(successCount), is("1"));
	}

	/*
	멀티 스레트 테스트 3 (스레드 1000개 자리는 하나로 고정하고 예약한다.)
	 */
	@Test
	@DisplayName("예약 - 멀티 스레드 테스트 - 3")
	void multiThreadTest3() throws InterruptedException {

		AtomicInteger successCount = new AtomicInteger(0);
		ExecutorService executor = Executors.newFixedThreadPool(1000);
		// 작업 스레드가 끝날 때 까지 기다리는 역할이다.
		CountDownLatch latch = new CountDownLatch(1000);

		for (int i = 0; i < 1000; i++) {
			int index = i + 1;
			executor.execute(() -> {
				try {
					final String uuid = UUID.randomUUID().toString();
					TicketingRequestDto dto = new TicketingRequestDto(uuid, "TEST", "TEST", "A1");

					TicketingRequestDto response = ticketingService.createTicket(dto);
					if(response != null) {
						successCount.getAndIncrement();
					}
					log.info("Thread " + index + " - 완료");
				} catch (Exception e) {
					log.error("[error] Thread " + index + " - " + e.getMessage());
				}
				latch.countDown();
			});
		}
		latch.await();

		assertThat(String.valueOf(successCount), is("1"));
	}

	/*
	멀티 스레트 테스트 4 (1000개 중 990개는 예약이고 10개는 예약 취소가 진행되는 스레드이다.)
	단, 예약 취소 같은 경우 500번 부터 시작되며 이미 있는 티켓 ID를 통하여 예약 취소가 진행되어진다.
	*/
	@Test
	@DisplayName("예약 & 예약 취소- 멀티 스레드 테스트")
	void multiThreadTest4() throws InterruptedException {
		AtomicInteger successCount = new AtomicInteger(0);
		ExecutorService executor = Executors.newFixedThreadPool(1000);
		CountDownLatch latch = new CountDownLatch(1000);

		List<Integer> cancelList = new ArrayList<>();
		cancelList.add(500);
		cancelList.add(600);
		cancelList.add(700);
		cancelList.add(800);
		cancelList.add(900);
		cancelList.add(523);
		cancelList.add(837);
		cancelList.add(765);
		cancelList.add(610);
		cancelList.add(924);

		for (int i = 0; i < 1000; i++) {
			int index = i + 1;
			if(cancelList.contains(i)) {
				executor.execute(() -> {
					try {
						TicketingRequestDto dto = new TicketingRequestDto(
								ticketRepository.findAll().get(0).getTicketId(), null, null, null
						);
						ticketingService.cancelTicket(dto);
						successCount.getAndIncrement();
						log.info("Thread " + index + " - 예약이 취소되었습니다.");
					} catch (Exception e) {
						log.error("[error] Thread " + index + " - " + e.getMessage());
					}
					latch.countDown();
				});
			} else {
				executor.execute(() -> {
					try {
						final String uuid = UUID.randomUUID().toString();
						final String seatNumber = String.valueOf(Math.round((Math.random() * 40) + 1));
						final String seat = String.valueOf((char)(Math.round((Math.random() * 14) + 65))) + seatNumber;
						String userId = String.valueOf((Math.round((Math.random() * 1000) + 1)));

						TicketingRequestDto dto = new TicketingRequestDto(uuid, userId, "TEST", seat);
						TicketingRequestDto response = ticketingService.createTicket(dto);
						log.info("Thread " + index + " -  완료");
					} catch (Exception e) {
						log.error("[error] Thread " + index + " - " + e.getMessage());
					}
					latch.countDown();
				});
			}
		}

		latch.await();

		// 예약 취소의 갯수를 확인한다.
		assertThat(String.valueOf(successCount), not("0"));
	}
}
