package app.ticket.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import app.ticket.StartApplication;
import app.ticket.ticketing.db.User;
import app.ticket.ticketing.user.UserCreateRequestDto;
import app.ticket.ticketing.user.UserRepository;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@Slf4j
@ContextConfiguration(classes = StartApplication.class)
public class UserUnitTests {

    /*
     * User 객체를 Test한다.
     */

    @Autowired
    private UserRepository userRepository;

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

    @DisplayName("user - 생성 테스트")
    @Test
    void createUserTest() {
        // given
        User newData = new User(setCreateDto());
        newData.setCreatedAt(new Date());

        // when
        User result = userRepository.saveAndFlush(newData);

        // then
        assertThat(result.getUserId().length(), is(13));
        assertThat(result.getName(), is("test"));

        userRepository.delete(newData);
    }

    @DisplayName("user - 잘못 된 값으로 생성된 테스트")
    @Test
    void createWrongUserTest() {
        // given
        User notIdData = new User();

        // when

        // then
        Assertions.assertThrows(JpaSystemException.class, () -> {
            userRepository.saveAndFlush(notIdData);
        });
    }

    @DisplayName("user - 단건 조회 테스트")
    @Test
    void readUserTest() {
        // when
        User result = userRepository.findByUserId(this.user.getUserId());

        // then
        assertThat(result.getUserId().length(), is(13));
        assertThat(result.getName(), is("test"));
        assertThat(result.getPoint(), is(50000));
        assertThat(result.getCreatedAt(), is(notNullValue()));
    }

    @DisplayName("user - 존재하지 않는 공연 조회 테스트")
    @Test
    void readWrongUserTest() {
        // given
        User testData = new User();

        // when
        User result = userRepository.findByUserId(testData.getUserId());

        // then
        Assertions.assertThrows(NullPointerException.class, () -> {
            assertThat(result.getUserId().length(), is(13));
        });
    }

    @DisplayName("user - 수정 테스트")
    @Test
    void updateUserTest() {
        // given
        User updateData = userRepository.findByUserId(this.user.getUserId());

        // when
        updateData.setName("updated");
        updateData.setPoint(30000);

        User result = userRepository.saveAndFlush(updateData);

        // then
        assertThat(result.getUserId().length(), is(13));
        assertThat(result.getName(), is("updated"));
        assertThat(result.getPoint(), is(30000));
        assertThat(result.getCreatedAt(), is(not(result.getUpdatedAt())));
    }

    @DisplayName("user - 삭제 테스트")
    @Test
    void deleteUserTest() {
        // when
        userRepository.delete(this.user);
        User result = userRepository.findByUserId(this.user.getUserId());

        // then
        assertThat(result, is(nullValue()));
    }

    @AfterEach()
    void deleteData() {
        userRepository.deleteAll();
    }
}
