package app.ticket.ticketing.user;

import app.ticket.ticketing.common.exception.custom.user.NotEnoughPointsException;
import app.ticket.ticketing.common.exception.custom.user.NotExistedUserDataException;
import app.ticket.ticketing.db.User;
import jakarta.transaction.Transactional;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional()
public class UserService {
    private final UserRepository userRepository;

    /*
     * 유저 ID를 기준으로 유저 정보를 조회한다.
     */
    public User readUser(String id) {
        User user = userRepository.findByUserIdAndIsDelete(id, false);
        if (user == null) {
            throw new NotExistedUserDataException(id);
        }
        return user;
    }

    /*
     * 유저 정보를 생성한다. (단, 포인트는 0으로 생성)
     */
    public UserResponseDto createUser(UserRequestDto request) {
        User newData = new User(request);
        userRepository.saveAndFlush(newData);
        return new UserResponseDto(newData);
    }

    /*
     * 유저 내 정보를 수정한다.
     */
    public UserResponseDto updateUser(UserRequestDto request) {
        User user = readUser(request.getUserId());
        user.setName(request.getName());
        userRepository.saveAndFlush(user);
        return new UserResponseDto(user);
    }

    /*
     * 유저 정보를 삭제한다. (soft-delete)
     */
    public void deleteUser(String userId) {
        User user = readUser(userId);
        user.setDeletedAt(new Date());
        user.setIsDelete(true);
        userRepository.saveAndFlush(user);
    }

    /*
     * 유저 내 포인트를 충전한다.
     */
    public UserResponseDto chargePoint(UserRequestDto request) {
        User user = readUser(request.getUserId());
        user.setPoint(request.getPoint() + user.getPoint());
        userRepository.saveAndFlush(user);
        return new UserResponseDto(user);
    }

    /*
     * 유저 내 포인트를 사용한다.
     */
    public UserResponseDto usePoint(UserRequestDto request) {
        User user = readUser(request.getUserId());
        if (request.getPoint() > user.getPoint()) {
            throw new NotEnoughPointsException(request.getUserId());
        }
        user.setPoint(user.getPoint() - request.getPoint());
        userRepository.saveAndFlush(user);
        return new UserResponseDto(user);
    }

}
