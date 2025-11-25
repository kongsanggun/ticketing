package app.ticket.ticketing.user;

import app.ticket.ticketing.common.exception.custom.user.NotPointRemainException;
import app.ticket.ticketing.common.exception.custom.user.UserNotDataException;
import app.ticket.ticketing.db.User;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserPointService {

    private final UserRepository userRepository;

    /*
     * 유저 내 포인트를 충전한다.
     */
    public UserResponseDto chargePoint(UserRequestDto request) {
        User user = checkExist(request);
        user.setPoint(request.getPoint() + user.getPoint());
        user.setUpdatedAt(new Date());
        userRepository.saveAndFlush(user);
        return new UserResponseDto(user);
    }

    /*
     * 유저 내 포인트를 사용한다.
     */
    public UserResponseDto usePoint(UserRequestDto request) {
        User user = checkExist(request);
        if (request.getPoint() > user.getPoint()) {
            throw new NotPointRemainException(request.getUserId());
        }
        user.setPoint(user.getPoint() - request.getPoint());
        user.setUpdatedAt(new Date());
        userRepository.saveAndFlush(user);
        return new UserResponseDto(user);
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
