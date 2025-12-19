package app.ticket.SeatClass;

import static org.hamcrest.Matchers.is;

import app.ticket.StartApplication;
import app.ticket.ticketing.common.exception.ExceptionCode;
import app.ticket.ticketing.seatclass.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@Slf4j
@ContextConfiguration(classes = StartApplication.class)
public class SeatClassE2ETests {

    /*
     * SeatClassController 내 api 관점에서 테스트 한다.
     */

    @Value("${environment.URL}")
    String baseURI;

    @Value("${server.port}")
    int port;

    @Autowired
    private SeatClassController seatClassController;

    @Autowired
    private SeatClassRepository seatClassRepository;

    public SeatClassRequestDto setParam(String concertId) {
        return new SeatClassRequestDto(
                "test",
                concertId,
                "test석",
                10000,
                100
        );
    }

    public SeatClassRequestDto setParam(SeatClassResponseDto responseData) {
        return new SeatClassRequestDto(
                responseData.getSeatClassId(),
                responseData.getConcertId(),
                "update석",
                10000,
                100
        );
    }

    public SeatClassResponseDto setData() {
        SeatClassRequestDto param = setParam("test");
        return seatClassController.createSeatClass(param);
    }

    public SeatClassResponseDto setDataforDelete() {
        SeatClassRequestDto param = setParam("test2");
        return seatClassController.createSeatClass(param);
    }

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = baseURI;
        RestAssured.port = port;
    }

    @DisplayName("[get] : /seat-class/{id} : 성공")
    @Test
    void readSeatClassSuccessTest() {
        setData();
        RestAssured.given().contentType(ContentType.JSON).when().get("/seat-class/test").then().statusCode(200);
    }

    @DisplayName("[get] : /seat-class/{id} : 실패 1 - 조회 값이 없을 때 ")
    @Test
    void readSeatClassFailTest1() {
        RestAssured.given().contentType(ContentType.JSON).when().get("/seat-class/wrongId").then().statusCode(404);
    }

    @DisplayName("[post] : /seat-class : 성공")
    @Test
    void createSeatClassSuccessTest() {
        SeatClassRequestDto testParam = setParam("test");
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().post("/seat-class").then()
                        .statusCode(201);
    }

    @DisplayName("[post] : /seat-class : 실패 1 - parameter 올바르지 않을 때")
    @Test
    void createSeatClassFailTest1() {
        SeatClassRequestDto testParam = new SeatClassRequestDto();
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().post("/seat-class").then()
                        .statusCode(400);
    }

    @DisplayName("[put] : /seat-class : 성공")
    @Test
    void updateSeatClassSuccessTest() {
        SeatClassResponseDto responseData = setData();
        SeatClassRequestDto testParam = setParam(responseData);

        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().put("/seat-class").then()
                        .statusCode(200);
    }

    @DisplayName("[put] : /seat-class : 실패 1 - 조회 값이 없을 때 ")
    @Test
    void updateSeatClassFailTest1() {
        SeatClassRequestDto testParam = new SeatClassRequestDto();
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().put("/seat-class").then()
                        .statusCode(404);
    }

    @DisplayName("[delete] : /seat-class : 성공")
    @Test
    void deleteSeatClassSuccessTest() {
        setData();
        SeatClassResponseDto responseData = setData();
        SeatClassRequestDto testParam = setParam(responseData);

        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().delete("/seat-class").then()
                        .statusCode(200);
    }

    @DisplayName("[delete] : /seat-class : 실패 1 - 조회 값이 없을 때 ")
    @Test
    void deleteSeatClassFailTest1() {
        SeatClassResponseDto responseData = setData();

        SeatClassRequestDto testParam = setParam(responseData);
        seatClassController.deleteSeatClass(testParam);

        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().delete("/seat-class").then()
                        .statusCode(404);
    }

    @DisplayName("[delete] : /seat-class : 실패 2 - 삭제 이후 ConcertId 기준 남아있는 stage 값이 존재하지 않을 때 에러를 발생한다.")
    @Test
    void deleteSeatClassFailTest2() {
        SeatClassResponseDto responseData = setDataforDelete();

        SeatClassRequestDto testParam = setParam(responseData);
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().delete("/seat-class").then()
                        .statusCode(422);
    }
}
