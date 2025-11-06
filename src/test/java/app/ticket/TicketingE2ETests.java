package app.ticket;

import app.ticket.ticketing.db.Ticket;
import app.ticket.ticketing.ticketing.TicketRepository;
import app.ticket.ticketing.ticketing.TicketingController;
import app.ticket.ticketing.ticketing.TicketingRequestDto;
import io.hypersistence.tsid.TSID;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.Matchers.is;

@SpringBootTest
@Slf4j
@ContextConfiguration(classes = StartApplication.class)
public class TicketingE2ETests {

    /*
       TicketingController 내 api 관점에서 테스트 한다.
    */

    @Value("${environment.URL}")
    String baseURI;

    @Value("${server.port}")
    int port;

    @Autowired
    private TicketingController ticketingController;

    @Autowired
    private TicketRepository ticketRepository;

    public TicketingRequestDto setParam() {
        final String seatNumber = String.valueOf(Math.round((Math.random() * 40) + 1));
        final String seat = String.valueOf((char)(Math.round((Math.random() * 14) + 65))) + seatNumber;

        TicketingRequestDto result = new TicketingRequestDto();
        result.setTicketId(TSID.fast().toString());
        result.setUserId(UUID.randomUUID().toString().substring(0, 13));
        result.setShowId("test");
        result.setSeat(seat);
        return result;
    }

    public TicketingRequestDto setData() {
        TicketingRequestDto param = setParam();
        Ticket ticket = new Ticket(param);
        ticket.setCreatedAt(new Date());

        ticketRepository.save(ticket);
        return param;
    }

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = baseURI;
        RestAssured.port = port;
    }

    @DisplayName("[get] : /ticket/{id} : 성공")
    @Test
    void checkTicketSuccessTest() {
        TicketingRequestDto testParam = setData();
        log.info(testParam.getTicketId());
        RestAssured.given()
                    .contentType(ContentType.JSON)
                .when()
                    .get("/ticket/" + testParam.getTicketId())
                .then()
                    .statusCode(200);
    }

    @DisplayName("[get] : /ticket/{id} : 실패 1 - 조회 값이 없을 때")
    @Test
    void checkTicketFailTest() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/ticket/wrongId")
                .then()
                    .statusCode(400)
                .and()
                    .body("message",is("해당 값이 존재하지 않습니다."));
    }

    @DisplayName("[post] : /ticket : 성공")
    @Test
    void createTicketSuccessTest() {
        TicketingRequestDto testParam = setParam();
        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(testParam)
                .when()
                    .post("/ticket")
                .then()
                    .statusCode(201);
    }

    @DisplayName("[post] : /ticket : 실패 1 - 중복된 유저요청일 때")
    @Test
    void createTicketFailTest1() {
        TicketingRequestDto testParam = setData();
        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(testParam)
                .when()
                    .post("/ticket")
                .then()
                    .statusCode(409)
                .and()
                    .body("message",is("이미 예약된 공연입니다."));
    }

    @DisplayName("[post] : /ticket : 실패 2 - 이미 선점된 자리일 때")
    @Test
    void createTicketFailTest2() {
        TicketingRequestDto testParam = setData();
        testParam.setUserId("test2");
        RestAssured.given()
                .contentType(ContentType.JSON)
                    .body(testParam)
                .when()
                    .post("/ticket")
                .then()
                    .statusCode(403)
                .and()
                    .body("message",is("이미 선점된 자리입니다."));
    }

    @DisplayName("[delete] : /ticket/{id} : 성공")
    @Test
    void cancelTicketSuccessTest() {
        TicketingRequestDto testParam = setData();
        RestAssured.given()
                .contentType(ContentType.JSON)
                    .body(testParam)
                .when()
                    .delete("/ticket")
                .then()
                    .statusCode(200);
    }

    @DisplayName("[delete] : /ticket/{id} : 실패 1 - 조회 값이 없을 때 ")
    @Test
    void cancelTicketFailTest1() {
        TicketingRequestDto testParam = setParam();
        testParam.setTicketId("wrongId");
        RestAssured.given()
                .contentType(ContentType.JSON)
                    .body(testParam)
                .when()
                    .delete("/ticket")
                .then()
                    .statusCode(400)
                .and()
                    .body("message",is("해당 값이 존재하지 않습니다."));
    }

    @AfterEach
    void deleteID() {
        ticketRepository.deleteAll();
    }
}
