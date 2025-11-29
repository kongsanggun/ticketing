package app.ticket.User;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import app.ticket.StartApplication;
import app.ticket.ticketing.common.exception.custom.user.NotEnoughPointsException;
import app.ticket.ticketing.common.exception.custom.user.NotExistedUserDataException;
import app.ticket.ticketing.db.User;
import app.ticket.ticketing.user.UserCreateRequestDto;
import app.ticket.ticketing.user.UserRepository;
import app.ticket.ticketing.user.UserRequestDto;
import app.ticket.ticketing.user.UserResponseDto;
import app.ticket.ticketing.user.UserService;
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
public class UserServiceTests {

    /*
     * UserService 및 UserPointService를 Test한다.
     */

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private User user;

    public UserCreateRequestDto setCreateDto() {
        return new UserCreateRequestDto("test");
    }

    UserRequestDto setRequestData() {
        UserRequestDto request = new UserRequestDto();
        request.setName("test");
        return request;
    }

    UserRequestDto setRequestData(User user) {
        UserRequestDto request = new UserRequestDto();
        request.setUserId(user.getUserId());
        request.setName(user.getName());
        request.setPoint(user.getPoint());
        return request;
    }

    UserRequestDto setRequestData(User user, int point) {
        UserRequestDto request = new UserRequestDto();
        request.setUserId(user.getUserId());
        request.setName(user.getName());
        request.setPoint(point);
        return request;
    }

    @BeforeEach()
    void setData() {
        User user = new User(setCreateDto());
        user.setPoint(50000);
        this.user = user;
        userRepository.saveAndFlush(user);
    }

    @DisplayName("user - 조회 서비스 테스트")
    @Test
    void readUserServiceTest() {
        // when
        User result = userService.readUser(this.user.getUserId());

        // then
        assertThat(result.getUserId().length(), is(13));

        assertThatThrownBy(() -> userService.readUser("wrongTest")).isInstanceOf(NotExistedUserDataException.class);
    }

    @DisplayName("user - 수정 서비스 테스트")
    @Test
    void updateUserServiceTest() {
        // when
        UserRequestDto data = setRequestData(this.user);
        data.setName("updated");
        UserResponseDto result = userService.updateUser(this.user.getUserId(), data);

        // then
        assertThat(result.getUserId(), is(notNullValue()));
        assertThat(result.getUserId().length(), is(13));
        assertThat(result.getName(), is("updated"));
    }

    @DisplayName("user - 삭제 서비스 테스트")
    @Test
    void deleteUserServiceTest() {
        // when
        String userId = this.user.getUserId();
        userService.deleteUser(userId);
        User userIdResult = userRepository.findByUserId(userId);
        User isDeleteResult = userRepository.findByUserIdAndIsDelete(userId, false);

        // then
        assertThat(userIdResult.getIsDelete(), is(true));
        assertThat(isDeleteResult, is(nullValue()));

        assertThatThrownBy(() -> userService.deleteUser(userId))
                        .isInstanceOf(NotExistedUserDataException.class);
    }

    @DisplayName("user - 포인트 충전 서비스 테스트")
    @Test
    void chargePointServiceTest() {
        // when
        UserRequestDto data = setRequestData(this.user);
        data.setPoint(10000);
        UserResponseDto result = userService.chargePoint(data);

        // then
        assertThat(result.getUserId(), is(notNullValue()));
        assertThat(result.getPoint(), is(60000));

        assertThatThrownBy(() -> userService.chargePoint(setRequestData())).isInstanceOf(
                NotExistedUserDataException.class);
    }

    @DisplayName("user - 포인트 사용 서비스 테스트")
    @Test
    void usePointServiceTest() {
        // when
        UserRequestDto data = setRequestData(this.user);
        data.setPoint(10000);
        UserResponseDto result = userService.usePoint(data);

        // then
        assertThat(result.getUserId(), is(notNullValue()));
        assertThat(result.getPoint(), is(40000));

        data.setPoint(50000);
        assertThatThrownBy(() -> userService.usePoint(data)).isInstanceOf(NotEnoughPointsException.class);

        assertThatThrownBy(() -> userService.usePoint(setRequestData())).isInstanceOf(NotExistedUserDataException.class);
    }

    @AfterEach()
    void deleteData() {
        userRepository.deleteAll();
    }
}
