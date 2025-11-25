package app.ticket.ticketing.user;

import app.ticket.ticketing.common.exception.custom.user.UserNotDataException;
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
        User user = userRepository.findByUserId(id);
        if (user == null) {
            throw new UserNotDataException(id);
        }
        return user;
    }

    /*
     * 유저 정보를 생성한다. (단, 포인트는 0으로 생성)
     */
    public UserResponseDto createUser(UserRequestDto request) {
        User newData = new User(request);
        newData.setCreatedAt(new Date());
        newData.setPoint(0);
        userRepository.saveAndFlush(newData);
        return new UserResponseDto(newData);
    }

    /*
     * 유저 내 정보를 수정한다.
     */
    public UserResponseDto updateUser(UserRequestDto request) {
        User user = checkExist(request);
        user.setName(request.getName());
        user.setUpdatedAt(new Date());
        userRepository.saveAndFlush(user);
        return new UserResponseDto(user);
    }

    /*
     * 유저 정보를 삭제한다.
     */
    public void deleteUser(UserRequestDto request) {
        checkExist(request);
        userRepository.deleteByUserId(request.getUserId());
    }

    /*
     * 존재하는 유저인지 확인한다.
     */
    private User checkExist(UserRequestDto request) {
        User user = userRepository.findByUserId(request.getUserId());
        if (user == null) {
            throw new UserNotDataException(request.getUserId());
        }
        return user;
    }

}
