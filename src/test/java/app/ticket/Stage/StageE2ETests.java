package app.ticket.Stage;

import static org.hamcrest.Matchers.is;

import app.ticket.StartApplication;
import app.ticket.ticketing.common.exception.ExceptionCode;
import app.ticket.ticketing.stage.StageController;
import app.ticket.ticketing.stage.StageRepository;
import app.ticket.ticketing.stage.StageRequestDto;
import app.ticket.ticketing.stage.StageResponseDto;
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
public class StageE2ETests {

    /*
     * StageController 내 api 관점에서 테스트 한다.
     */

    @Value("${environment.URL}")
    String baseURI;

    @Value("${server.port}")
    int port;

    @Autowired
    private StageController stageController;

    @Autowired
    private StageRepository stageRepository;

    public StageRequestDto setParam(String concertId) {
        return new StageRequestDto(
                "test",
                concertId,
                new Date()
        );
    }

    public StageRequestDto setParam(StageResponseDto responseDto) {
        return new StageRequestDto(
                responseDto.getStageId(),
                responseDto.getConcertId(),
                new Date()
        );
    }

    public StageResponseDto setData() {
        StageRequestDto param = setParam("test");
        return stageController.createStage(param);
    }

    public StageResponseDto setDataforDelete() {
        StageRequestDto param = setParam("test2");
        return stageController.createStage(param);
    }

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = baseURI;
        RestAssured.port = port;
    }

    @DisplayName("[get] : /stage/{id} : 성공")
    @Test
    void readStagesSuccessTest() {
        setData();
        RestAssured.given().contentType(ContentType.JSON).when().get("/stage/test").then().statusCode(200);
    }

    @DisplayName("[get] : /stage/{id} : 실패 1 - 조회 값이 없을 때 ")
    @Test
    void readStagesFailTest1() {
        RestAssured.given().contentType(ContentType.JSON).when().get("/stage/wrongId").then().statusCode(404).and()
                        .body("message", is("해당 값이 존재하지 않습니다."));
    }

    @DisplayName("[post] : /stage : 성공")
    @Test
    void createStageSuccessTest() {
        StageRequestDto testParam = setParam("test");
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().post("/stage").then().statusCode(201);
    }

    @DisplayName("[post] : /stage : 실패 1 - parameter 올바르지 않을 때")
    @Test
    void createStageFailTest1() {
        StageRequestDto testParam = new StageRequestDto();
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().post("/stage").then().statusCode(400);
    }

    @DisplayName("[put] : /stage : 성공")
    @Test
    void updateStageSuccessTest() {
        StageResponseDto responseData = setData();
        StageRequestDto testParam = setParam(responseData);
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().put("/stage").then().statusCode(200);
    }

    @DisplayName("[put] : /stage : 실패 1 - 조회 값이 없을 때 ")
    @Test
    void updateStageFailTest1() {
        StageRequestDto testParam = new StageRequestDto();
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().put("/stage").then().statusCode(404)
                        .and().body("message", is("해당 값이 존재하지 않습니다."));
    }

    @DisplayName("[delete] : /stage : 성공")
    @Test
    void deleteStageSuccessTest() {
        setData();
        StageResponseDto responseData = setData();
        StageRequestDto testParam = setParam(responseData);
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().delete("/stage").then()
                        .statusCode(200);
    }

    @DisplayName("[delete] : /stage : 실패 1 - 조회 값이 없을 때 ")
    @Test
    void deleteStageFailTest1() {
        StageResponseDto responseData = setData();
        StageRequestDto testParam = setParam(responseData);
        stageController.deleteStage(testParam);

        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().delete("/stage").then().statusCode(404)
                        .and().body("message", is("해당 값이 존재하지 않습니다."));
    }

    @DisplayName("[delete] : /stage : 실패 2 - 삭제 이후 ConcertId 기준 남아있는 stage 값이 존재하지 않을 때 에러를 발생한다.")
    @Test
    void deleteStageFailTest2() {
        StageResponseDto responseDto = setDataforDelete();
        StageRequestDto testParam = setParam(responseDto);

        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().delete("/stage").then().statusCode(422)
                        .and().body("message", is(ExceptionCode.EMPTY_STAGE.getMessage()));
    }
}
