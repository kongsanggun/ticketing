package app.ticket.User;

import static org.hamcrest.Matchers.is;

import app.ticket.StartApplication;
import app.ticket.ticketing.db.User;
import app.ticket.ticketing.user.UserCreateRequestDto;
import app.ticket.ticketing.user.UserPointRequestDto;
import app.ticket.ticketing.user.UserRepository;
import app.ticket.ticketing.user.UserPutRequestDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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

    public UserPutRequestDto setPutParam() {
        return new UserPutRequestDto("testUpdated");
    }

    public UserCreateRequestDto setCreateDto() {
        return new UserCreateRequestDto("test");
    }

    public User setData() {
        User param = new User(setCreateDto());
        param.chargePoint(50000);
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
        RestAssured.given().contentType(ContentType.JSON).when().get("/user/wrongId").then().statusCode(404);
    }

    @DisplayName("[post] : /user : 성공")
    @Test
    void createUserSuccessTest() {
        UserCreateRequestDto testParam = setCreateDto();
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().post("/user").then().statusCode(201);
    }

    @DisplayName("[put] : /user/{id} : 성공")
    @Test
    void updateUserSuccessTest() {
        User user = setData();
        UserPutRequestDto testParam = setPutParam();
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().put("/user/" + user.getUserId()).then().statusCode(200);
    }

    @DisplayName("[put] : /user/{id} : 실패 1 - 조회 값이 없을 때 ")
    @Test
    void updateUserFailTest1() {
        UserPutRequestDto testParam = new UserPutRequestDto();
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().put("/user/wrongId").then().statusCode(404);
    }

    @DisplayName("[delete] : /user/{id} : 성공")
    @Test
    void deleteUserSuccessTest() {
        User user = setData();
        RestAssured.given().contentType(ContentType.JSON).when().delete("/user/" + user.getUserId()).then()
                        .statusCode(204);
    }

    @DisplayName("[delete] : /user/{id} : 실패 1 - 조회 값이 없을 때 ")
    @Test
    void deleteUserFailTest1() {
        RestAssured.given().contentType(ContentType.JSON).when().delete("/user/wrongId").then().statusCode(404);
    }

    @DisplayName("[post] : /point/charge : 성공")
    @Test
    void chargePointSuccessTest() {
        User user = setData();
        UserPointRequestDto testParam = new UserPointRequestDto(user.getUserId(), 10000);
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().post("/point/charge").then()
                .statusCode(200);
    }

    @DisplayName("[post] : /point/charge : 실패 1 - parameter이 올바르지 않을 때")
    @Test
    void chargePointFailTest1() {
        User user = setData();
        UserPointRequestDto testParam = new UserPointRequestDto(user.getUserId(), -1);
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().post("/point/charge").then()
                .statusCode(400);
    }

    @DisplayName("[post] : /point/use : 성공")
    @Test
    void usePointSuccessTest() {
        User user = setData();
        UserPointRequestDto testParam = new UserPointRequestDto(user.getUserId(), 10000);
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().post("/point/use").then()
                .statusCode(200);
    }

    @DisplayName("[post] : /point/use : 실패 1 - parameter이 올바르지 않을 때")
    @Test
    void usePointFailTest1() {
        User user = setData();
        UserPointRequestDto testParam = new UserPointRequestDto(user.getUserId(), -1);
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().post("/point/use").then()
                .statusCode(400);
    }

    @DisplayName("[post] : /point/use : 실패 2 - 잔여 금액이 충분하지 않을 때")
    @Test
    void usePointFailTest2() {
        User user = setData();
        UserPointRequestDto testParam = new UserPointRequestDto(user.getUserId(), 50001);
        RestAssured.given().contentType(ContentType.JSON).body(testParam).when().post("/point/use").then()
                .statusCode(422);
    }
}
