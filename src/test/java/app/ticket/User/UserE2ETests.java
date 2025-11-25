package app.ticket.User;

import static org.hamcrest.Matchers.is;

import app.ticket.StartApplication;
import app.ticket.ticketing.common.exception.ExceptionCode;
import app.ticket.ticketing.db.User;
import app.ticket.ticketing.stage.StageController;
import app.ticket.ticketing.stage.StageRepository;
import app.ticket.ticketing.stage.StageRequestDto;
import app.ticket.ticketing.stage.StageResponseDto;
import app.ticket.ticketing.user.UserController;
import app.ticket.ticketing.user.UserRepository;
import app.ticket.ticketing.user.UserRequestDto;
import app.ticket.ticketing.user.UserResponseDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
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
public class UserE2ETests {

    /*
     * UserController 내 api 관점에서 테스트 한다.
     */

    @Value("${environment.URL}")
    String baseURI;

    @Value("${server.port}")
    int port;

    @Autowired
    private UserRepository userRepository;

    public UserRequestDto setParam() {
        UserRequestDto result = new UserRequestDto();
        result.setName("test");
        result.setPoint(50000);
        return result;
    }

    public User setData() {
        User param = new User(setParam());
        param.setCreatedAt(new Date());
        userRepository.save(param);
        return param;
    }

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = baseURI;
        RestAssured.port = port;
    }

    @DisplayName("[get] : /user/{id} : 성공")
    @Test
    void readUserSuccessTest() {
        User user = setData();
        RestAssured.given().contentType(ContentType.JSON).when().get("/user/" + user.getUserId()).then().statusCode(200);
    }

    @DisplayName("[get] : /user/{id} : 실패 1 - 조회 값이 없을 때 ")
    @Test
    void readUserFailTest1() {
        RestAssured.given().contentType(ContentType.JSON).when().get("/user/wrongId").then().statusCode(400).and()
                        .body("message", is("해당 값이 존재하지 않습니다."));
    }

    @DisplayName("[post] : /user : 성공")
    @Test
    void createUserSuccessTest() {
        UserRequestDto testParam = setParam();
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().post("/user").then().statusCode(201);
    }

    @DisplayName("[put] : /user : 성공")
    @Test
    void updateUserSuccessTest() {
        User user = setData();
        UserRequestDto testParam = setParam();
        testParam.setUserId(user.getUserId());
        testParam.setName("testUpdated");

        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().put("/user").then().statusCode(200);
    }

    @DisplayName("[put] : /stage : 실패 1 - 조회 값이 없을 때 ")
    @Test
    void updateUserFailTest1() {
        UserRequestDto testParam = new UserRequestDto();
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().put("/user").then().statusCode(400)
                        .and().body("message", is("해당 값이 존재하지 않습니다."));
    }

    @DisplayName("[delete] : /stage : 성공")
    @Test
    void deleteUserSuccessTest() {
        User user = setData();
        UserRequestDto testParam = setParam();
        testParam.setUserId(user.getUserId());

        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().delete("/user").then()
                        .statusCode(200);
    }

    @DisplayName("[delete] : /stage : 실패 1 - 조회 값이 없을 때 ")
    @Test
    void deleteUserFailTest1() {
        UserRequestDto testParam = setParam();
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().delete("/user").then().statusCode(400)
                        .and().body("message", is("해당 값이 존재하지 않습니다."));
    }

    @DisplayName("[put] : /point/charge : 성공")
    @Test
    void chargePointSuccessTest() {
        User user = setData();
        UserRequestDto testParam = setParam();
        testParam.setUserId(user.getUserId());
        testParam.setPoint(10000);

        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().put("/point/charge").then()
                .statusCode(200);
    }

    @DisplayName("[put] : /point/charge : 실패 1 - parameter이 올바르지 않을 때")
    @Test
    void chargePointFailTest1() {
        User user = setData();
        UserRequestDto testParam = setParam();
        testParam.setUserId(user.getUserId());
        testParam.setPoint(-1);

        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().put("/point/charge").then()
                .statusCode(400);
    }

    @DisplayName("[put] : /point/use : 성공")
    @Test
    void usePointSuccessTest() {
        User user = setData();
        UserRequestDto testParam = setParam();
        testParam.setUserId(user.getUserId());
        testParam.setPoint(10000);

        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().put("/point/use").then()
                .statusCode(200);
    }

    @DisplayName("[put] : /point/use : 실패 1 - parameter이 올바르지 않을 때")
    @Test
    void usePointFailTest1() {
        User user = setData();
        UserRequestDto testParam = setParam();
        testParam.setUserId(user.getUserId());
        testParam.setPoint(-1);

        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().put("/point/use").then()
                .statusCode(400);
    }

    @DisplayName("[put] : /point/use : 실패 2 - 잔여 금액이 충분하지 않을 때")
    @Test
    void usePointFailTest2() {
        User user = setData();
        UserRequestDto testParam = setParam();
        testParam.setUserId(user.getUserId());
        testParam.setPoint(50001);

        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().put("/point/use").then()
                .statusCode(422)
                .and().body("message", is("잔여 포인트가 충분하지 않습니다."));
    }
}
