package app.ticket.ticketing.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    /*
     * 유저 ID를 기준으로 유저 정보를 조회한다.
     */
    @GetMapping("/user/{id}")
    public UserResponseDto readUser(@PathVariable final String id) {
        return new UserResponseDto(userService.readUser(id));
    }

    /*
     * 유저 정보를 생성한다. (단, 포인트는 0으로 생성)
     */
    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody final UserRequestDto request) {
        return userService.createUser(request);
    }

    /*
     * 유저 내 정보를 수정한다. (포인트 제외)
     */
    @PutMapping("/user")
    public UserResponseDto updateUser(@RequestBody final UserRequestDto request) {
        return userService.updateUser(request);
    }

    /*
     * 유저 정보를 삭제한다.
     */
    @DeleteMapping("/user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@Valid @RequestBody final UserRequestDto request) {
        userService.deleteUser(request.getUserId());
    }

    /*
     * 유저 내 포인트를 충전한다.
     */
    @PostMapping("/point/charge")
    public UserResponseDto chargePoint(@Valid @RequestBody final UserRequestDto request) {
        return userService.chargePoint(request);
    }

    /*
     * 유저 내 포인트를 사용한다.
     */
    @PostMapping("/point/use")
    public UserResponseDto usePoint(@Valid @RequestBody final UserRequestDto request) {
        return userService.usePoint(request);
    }

}
