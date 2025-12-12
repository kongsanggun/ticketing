package app.ticket.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import app.ticket.StartApplication;
import app.ticket.ticketing.db.User;
import app.ticket.ticketing.user.UserController;
import app.ticket.ticketing.user.UserCreateRequestDto;
import app.ticket.ticketing.user.UserPointRequestDto;
import app.ticket.ticketing.user.UserRepository;
import app.ticket.ticketing.user.UserPutRequestDto;
import app.ticket.ticketing.user.UserResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@Slf4j
@ContextConfiguration(classes = StartApplication.class)
public class UserControllerTests {

    /*
     * UserController를 Test한다.
     */

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserController userController;

    private User user;

    public UserCreateRequestDto setCreateDto() {
        return new UserCreateRequestDto("test");
    }

    @BeforeEach()
    void setData() {
        User user = new User(setCreateDto());
        user.setPoint(50000);
        this.user = user;
        userRepository.saveAndFlush(user);
    }

    @DisplayName("user - 생성 컨트롤러 테스트")
    @Test
    void createUserControllerTest() {
        // when
        UserCreateRequestDto dto = new UserCreateRequestDto("test");
        UserResponseDto result = userController.createUser(dto);

        // then
        assertThat(result.getUserId().length(), is(13));
        assertThat(result.getName(), is("test"));
        assertThat(result.getPoint(), is(0));
    }

    @DisplayName("user - 조회 컨트롤러 테스트")
    @Test
    void readUserControllerTest() {
        // when
        UserResponseDto result = userController.readUser(this.user.getUserId());

        // then
        assertThat(result.getUserId().length(), is(13));
        assertThat(result.getName(), is("test"));
        assertThat(result.getPoint(), is(50000));
    }

    @DisplayName("user - 수정 컨트롤러 테스트")
    @Test
    void updateUserControllerTest() {
        // when
        UserPutRequestDto data = new UserPutRequestDto("updated");
        UserResponseDto result = userController.updateUser(this.user.getUserId(), data);

        // then
        assertThat(result.getUserId().length(), is(13));
        assertThat(result.getName(), is("updated"));
    }

    @DisplayName("user - 삭제 컨트롤러 테스트")
    @Test
    void deleteStageControllerTest() {
        // when
        userController.deleteUser(this.user.getUserId());
        User userIdResult = userRepository.findByUserId(this.user.getUserId());
        User isDeleteResult = userRepository.findByUserIdAndIsDelete(this.user.getUserId(), false);
        // then
        assertThat(userIdResult.getIsDelete(), is(true));
        assertThat(isDeleteResult, is(nullValue()));
    }

    @DisplayName("user - 포인트 충전 컨트롤러 테스트")
    @Test
    void chargePointControllerTest() {
        // when
        UserPointRequestDto data = new UserPointRequestDto(this.user.getUserId(), 10000);
        UserResponseDto result = userController.chargePoint(data);

        // then
        assertThat(result.getUserId().length(), is(13));
        assertThat(result.getPoint(), is(60000));
    }

    @DisplayName("user - 포인트 사용 컨트롤러 테스트")
    @Test
    void usePointControllerTest() {
        // when
        UserPointRequestDto data = new UserPointRequestDto(this.user.getUserId(), 10000);
        UserResponseDto result = userController.usePoint(data);

        // then
        assertThat(result.getUserId().length(), is(13));
        assertThat(result.getPoint(), is(40000));
    }

    @AfterEach()
    void deleteData() {
        userRepository.deleteAll();
    }
}
