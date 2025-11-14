package app.ticket.Concert;

import static org.hamcrest.Matchers.is;

import app.ticket.StartApplication;
import app.ticket.ticketing.concert.*;
import app.ticket.ticketing.seatclass.SeatClassRepository;
import app.ticket.ticketing.stage.StageRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@Slf4j
@ContextConfiguration(classes = StartApplication.class)
public class ConcertE2ETests {

    /*
     * ConcertService 내 api 관점에서 테스트 한다.
     */

    @Value("${environment.URL}")
    String baseURI;

    @Value("${server.port}")
    int port;

    @Autowired
    private ConcertController concertController;

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private SeatClassRepository seatClassRepository;

    @Autowired
    private ConcertRepository concertRepository;

    public ConcertRequestDto setParam() {
        ConcertRequestDto result = new ConcertRequestDto();
        result.setConcertId("test");
        result.setName("test");
        result.setDetail("테스트입니다.");
        result.setBookStartTime(new Date());
        result.setStageTime(new Date());
        result.setPriceName("U석");
        result.setPrice(39800);
        return result;
    }

    public ConcertResponseDto setData() {
        ConcertRequestDto param = setParam();
        return concertController.createConcert(param);
    }

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = baseURI;
        RestAssured.port = port;
    }

    @DisplayName("[get] : /concert/{id} : 성공")
    @Test
    void readConcertSuccessTest() {
        ConcertResponseDto responseData = setData();
        RestAssured.given().contentType(ContentType.JSON).when().get("/concert/" + responseData.getConcertId()).then()
                        .statusCode(200).and().body("name", is("test"));
    }

    @DisplayName("[get] : /concert/{id} : 실패 1 - 조회 값이 없을 때 ")
    @Test
    void readConcertFailTest1() {
        RestAssured.given().contentType(ContentType.JSON).when().get("/concert/wrongId").then().statusCode(400).and()
                        .body("message", is("해당 값이 존재하지 않습니다."));
    }

    @DisplayName("[post] : /concert : 성공")
    @Test
    void createConcertSuccessTest() {
        ConcertRequestDto testParam = setParam();
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().post("/concert").then().statusCode(201)
                        .and().body("name", is("test"));
    }

    @DisplayName("[post] : /concert : 실패 1 - parameter 올바르지 않을 때")
    @Test
    void createConcertFailTest1() {
        ConcertRequestDto testParam = new ConcertRequestDto();
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().post("/concert").then()
                        .statusCode(400);
    }

    @DisplayName("[post] : /concert : 실패 2 - 중복된 값을 요청할 때")
    @Test
    void createConcertFailTest2() {
        ConcertResponseDto responseData = setData();
        ConcertRequestDto duplicateParam = setParam();
        duplicateParam.setConcertId(responseData.getConcertId());

        RestAssured.given().contentType(ContentType.JSON).body(duplicateParam).when().post("/concert").then()
                        .statusCode(409).and().body("message", is("이미 추가된 공연입니다."));
    }

    @DisplayName("[put] : /concert : 성공")
    @Test
    void updateConcertSuccessTest() {
        ConcertResponseDto responseData = setData();
        ConcertRequestDto testParam = setParam();
        testParam.setConcertId(responseData.getConcertId());
        testParam.setName("test update");

        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().put("/concert").then().statusCode(200)
                        .and().body("name", is("test update"));
    }

    @DisplayName("[put] : /concert : 실패 1 - 조회 값이 없을 때 ")
    @Test
    void updateConcertFailTest1() {
        ConcertRequestDto testParam = new ConcertRequestDto();
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().put("/concert").then().statusCode(400)
                        .and().body("message", is("해당 값이 존재하지 않습니다."));
    }

    @DisplayName("[delete] : /concert : 성공")
    @Test
    void deleteConcertSuccessTest() {
        ConcertResponseDto responseData = setData();
        ConcertRequestDto testParam = setParam();
        testParam.setConcertId(responseData.getConcertId());

        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().delete("/concert").then()
                        .statusCode(200);
    }

    @DisplayName("[delete] : /concert : 실패 1 - 조회 값이 없을 때 ")
    @Test
    void deleteConcertFailTest1() {
        ConcertRequestDto testParam = new ConcertRequestDto();
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().delete("/concert").then()
                        .statusCode(400).and().body("message", is("해당 값이 존재하지 않습니다."));
    }

    @AfterEach()
    void deleteData() {
        concertRepository.deleteAll();
    }
}
