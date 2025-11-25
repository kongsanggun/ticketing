package app.ticket.User;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import app.ticket.StartApplication;
import app.ticket.ticketing.common.exception.custom.stage.StageIdNotDataException;
import app.ticket.ticketing.common.exception.custom.stage.StageNotRemainException;
import app.ticket.ticketing.common.exception.custom.user.NotPointRemainException;
import app.ticket.ticketing.common.exception.custom.user.UserNotDataException;
import app.ticket.ticketing.db.Stage;
import app.ticket.ticketing.db.User;
import app.ticket.ticketing.stage.StageRepository;
import app.ticket.ticketing.stage.StageRequestDto;
import app.ticket.ticketing.stage.StageResponseDto;
import app.ticket.ticketing.stage.StageService;
import app.ticket.ticketing.user.UserPointService;
import app.ticket.ticketing.user.UserRepository;
import app.ticket.ticketing.user.UserRequestDto;
import app.ticket.ticketing.user.UserResponseDto;
import app.ticket.ticketing.user.UserService;
import io.hypersistence.tsid.TSID;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @Autowired
    private UserPointService userPointService;

    private User user;

    UserRequestDto setRequestData() {
        UserRequestDto request = new UserRequestDto();
        request.setName("test");
        request.setPoint(50000);
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
        User user = new User(setRequestData());
        user.setCreatedAt(new Date());
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

        assertThatThrownBy(() -> userService.readUser("wrongTest")).isInstanceOf(UserNotDataException.class);
    }

    @DisplayName("user - 수정 서비스 테스트")
    @Test
    void updateUserServiceTest() {
        // when
        UserRequestDto data = setRequestData(this.user);
        data.setName("updated");
        UserResponseDto result = userService.updateUser(data);

        // then
        assertThat(result.getUserId(), is(notNullValue()));
        assertThat(result.getUserId().length(), is(13));
        assertThat(result.getName(), is("updated"));
    }

    @DisplayName("user - 삭제 서비스 테스트")
    @Test
    void deleteUserServiceTest() {
        // when
        UserRequestDto data = setRequestData(this.user);
        userService.deleteUser(data);
        User result = userRepository.findByUserId(this.user.getUserId());

        // then
        assertThat(result, is(nullValue()));

        assertThatThrownBy(() -> userService.deleteUser(setRequestData(this.user)))
                        .isInstanceOf(UserNotDataException.class);
    }

    @DisplayName("user - 포인트 충전 서비스 테스트")
    @Test
    void chargePointServiceTest() {
        // when
        UserRequestDto data = setRequestData(this.user);
        data.setPoint(10000);
        UserResponseDto result = userPointService.chargePoint(data);

        // then
        assertThat(result.getUserId(), is(notNullValue()));
        assertThat(result.getPoint(), is(60000));

        assertThatThrownBy(() -> userPointService.chargePoint(setRequestData())).isInstanceOf(UserNotDataException.class);
    }

    @DisplayName("user - 포인트 사용 서비스 테스트")
    @Test
    void usePointServiceTest() {
        // when
        UserRequestDto data = setRequestData(this.user);
        data.setPoint(10000);
        UserResponseDto result = userPointService.usePoint(data);

        // then
        assertThat(result.getUserId(), is(notNullValue()));
        assertThat(result.getPoint(), is(40000));

        data.setPoint(50000);
        assertThatThrownBy(() -> userPointService.usePoint(data)).isInstanceOf(NotPointRemainException.class);

        assertThatThrownBy(() -> userPointService.usePoint(setRequestData())).isInstanceOf(UserNotDataException.class);
    }

    @AfterEach()
    void deleteData() {
        userRepository.deleteAll();
    }
}
