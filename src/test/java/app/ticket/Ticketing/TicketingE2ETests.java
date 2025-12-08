package app.ticket.Ticketing;

import static org.hamcrest.Matchers.is;

import app.ticket.StartApplication;
import app.ticket.ticketing.db.Ticket;
import app.ticket.ticketing.ticketing.TicketRepository;
import app.ticket.ticketing.ticketing.TicketingRequestDto;
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

@SpringBootTest
@Slf4j
@ContextConfiguration(classes = StartApplication.class)
public class TicketingE2ETests extends TicketingTests {

    /*
     * TicketingController 내 api 관점에서 테스트 한다.
     */

    @Value("${environment.URL}")
    String baseURI;

    @Value("${server.port}")
    int port;

    @Autowired
    private TicketRepository ticketRepository;

    public TicketingRequestDto setParam(String ticketId) {
        return new TicketingRequestDto(
                ticketId,
                "test",
                "test",
                "test",
                "test"
        );
    }

    public Ticket setData() {
        Ticket ticket = new Ticket(setParam("test"), 1);
        ticketRepository.save(ticket);
        return ticket;
    }

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = baseURI;
        RestAssured.port = port;
    }

    @DisplayName("[get] : /ticket/{id} : 성공")
    @Test
    void checkTicketSuccessTest() {
        Ticket ticket = setData();
        RestAssured.given().contentType(ContentType.JSON).when().get("/ticket/" + ticket.getTicketId()).then()
                        .statusCode(200);
    }

    @DisplayName("[get] : /ticket/{id} : 실패 1 - 조회 값이 없을 때")
    @Test
    void checkTicketFailTest() {
        RestAssured.given().contentType(ContentType.JSON).when().get("/ticket/wrongId").then().statusCode(404).and()
                        .body("message", is("해당 값이 존재하지 않습니다."));
    }

    @DisplayName("[post] : /ticket/{seat} : 성공")
    @Test
    void createSeatedTicketSuccessTest() {
        TicketingRequestDto testParam = setParam(null);
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().post("/ticket/1").then().statusCode(201);
    }

    @DisplayName("[post] : /ticket/{seat} : 실패 1 - 중복된 유저요청일 때")
    @Test
    void createTicketFailTest1() {
        setData();
        TicketingRequestDto testParam = setParam(null);
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().post("/ticket/1").then().statusCode(403)
                        .and().body("message", is("이미 선점된 자리입니다."));
    }

    @DisplayName("[post] : /ticket/random : 성공")
    @Test
    void createTicketSuccessTest() {
        TicketingRequestDto testParam = setParam(null);
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().post("/ticket/random").then().statusCode(201);
    }

    @DisplayName("[delete] : /ticket/{id} : 성공")
    @Test
    void cancelTicketSuccessTest() {
        Ticket ticket = setData();
        TicketingRequestDto testParam = setParam(ticket.getTicketId());

        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().delete("/ticket").then()
                        .statusCode(200);
    }

    @DisplayName("[delete] : /ticket/{id} : 실패 1 - 조회 값이 없을 때 ")
    @Test
    void cancelTicketFailTest1() {
        TicketingRequestDto testParam = setParam("wrongId");
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().delete("/ticket").then()
                        .statusCode(404).and().body("message", is("해당 값이 존재하지 않습니다."));
    }

    @AfterEach
    void deleteID() {
        ticketRepository.deleteAll();
    }
}
