package app.ticket.ticketing.user;

import app.ticket.ticketing.common.exception.custom.user.NotEnoughPointsException;
import app.ticket.ticketing.common.exception.custom.user.NotExistedUserDataException;
import app.ticket.ticketing.db.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional()
public class UserService {
    private final UserRepository userRepository;

    /*
     * 유저 ID를 기준으로 유저 정보를 조회한다.
     */
    public User readUser(String id) {
        return findUser(id);
    }

    /*
     * 유저 정보를 생성한다. (단, 포인트는 0으로 생성)
     */
    public UserResponseDto createUser(UserCreateRequestDto request) {
        User newData = new User(request);
        userRepository.saveAndFlush(newData);
        return new UserResponseDto(newData);
    }

    /*
     * 유저 내 정보를 수정한다.
     */
    public UserResponseDto updateUser(String userId, UserPutRequestDto request) {
        User user = findUser(userId);
        user.putName(request);
        userRepository.saveAndFlush(user);
        return new UserResponseDto(user);
    }

    /*
     * 유저 정보를 삭제한다. (soft-delete)
     */
    public void deleteUser(String userId) {
        User user = findUser(userId);
        user.deleteData();
        userRepository.saveAndFlush(user);
    }

    /*
     * 유저 내 포인트를 충전한다.
     */
    public UserResponseDto chargePoint(UserPointRequestDto request) {
        User user = findUser(request.getUserId());
        user.chargePoint(request.getPoint());
        userRepository.saveAndFlush(user);
        return new UserResponseDto(user);
    }

    /*
     * 유저 내 포인트를 사용한다.
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public UserResponseDto usePoint(UserPointRequestDto request) {
        User user = findUser(request.getUserId());
        user.usePoint(request.getPoint());
        userRepository.saveAndFlush(user);
        return new UserResponseDto(user);
    }

    /*
     * 유저 ID를 기준으로 유저 정보를 조회한다.
     */
    private User findUser(String userId) {
        User result = userRepository.findByUserIdAndIsDelete(userId, false);
        if (result == null) {
            throw new NotExistedUserDataException(userId);
        }
        return result;
    }
}
