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
import app.ticket.ticketing.user.UserPointRequestDto;
import app.ticket.ticketing.user.UserRepository;
import app.ticket.ticketing.user.UserPutRequestDto;
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

    UserPutRequestDto setRequestData() {
        return new UserPutRequestDto("test");
    }

    @BeforeEach()
    void setData() {
        User user = new User(setCreateDto());
        user.chargePoint(50000);
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
        UserPutRequestDto data = new UserPutRequestDto("updated");
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
        UserPointRequestDto data = new UserPointRequestDto(this.user.getUserId(), 10000);
        UserPointRequestDto wrongData = new UserPointRequestDto("wrongId", 10000);
        UserResponseDto result = userService.chargePoint(data);

        // then
        assertThat(result.getUserId(), is(notNullValue()));
        assertThat(result.getPoint(), is(60000));

        assertThatThrownBy(() -> userService.chargePoint(wrongData)).isInstanceOf(
                NotExistedUserDataException.class);
    }

    @DisplayName("user - 포인트 사용 서비스 테스트")
    @Test
    void usePointServiceTest() {
        // when
        UserPointRequestDto dto = new UserPointRequestDto(this.user.getUserId(), 10000);
        UserResponseDto result = userService.usePoint(dto);

        // then
        assertThat(result.getUserId(), is(notNullValue()));
        assertThat(result.getPoint(), is(40000));

        assertThatThrownBy(() -> {
            UserPointRequestDto overDto = new UserPointRequestDto(this.user.getUserId(), 50000);
            userService.usePoint(overDto);
        }).isInstanceOf(NotEnoughPointsException.class);

        assertThatThrownBy(() -> {
            UserPointRequestDto wrongDto = new UserPointRequestDto("wrongId", 10000);
            userService.usePoint(wrongDto);
        }).isInstanceOf(NotExistedUserDataException.class);
    }

    @AfterEach()
    void deleteData() {
        userRepository.deleteAll();
    }
}
