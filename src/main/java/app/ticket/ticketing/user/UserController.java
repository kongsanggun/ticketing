package app.ticket.ticketing.user;

import app.ticket.ticketing.common.exception.ApiErrorCode;
import app.ticket.ticketing.common.exception.ExceptionCode;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "User API", description = "사용자를 관리해주는 API입니다.")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    /*
     * 유저 ID를 기준으로 유저 정보를 조회한다.
     */
    @ApiErrorCode(value = {ExceptionCode.NOT_DATA})
    @GetMapping("/user/{id}")
    public UserResponseDto readUser(@PathVariable final String id) {
        return new UserResponseDto(userService.readUser(id));
    }

    /*
     * 유저 정보를 생성한다. (단, 포인트는 0으로 생성)
     */
    @ApiErrorCode(value = {ExceptionCode.NOT_DATA})
    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@Valid @RequestBody final UserCreateRequestDto request) {
        return userService.createUser(request);
    }

    /*
     * 유저 내 정보를 수정한다. (포인트 제외)
     */
    @ApiErrorCode(value = {ExceptionCode.NOT_DATA})
    @PutMapping("/user/{id}")
    public UserResponseDto updateUser(@PathVariable final String id, @RequestBody final UserPutRequestDto request) {
        return userService.updateUser(id, request);
    }

    /*
     * 유저 정보를 삭제한다.
     */
    @ApiErrorCode(value = {ExceptionCode.NOT_DATA})
    @DeleteMapping("/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable final String id) {
        userService.deleteUser(id);
    }

    /*
     * 유저 내 포인트를 충전한다.
     */
    @ApiErrorCode(value = {ExceptionCode.NOT_DATA})
    @PostMapping("/point/charge")
    public UserResponseDto chargePoint(@Valid @RequestBody final UserPointRequestDto request) {
        return userService.chargePoint(request);
    }

    /*
     * 유저 내 포인트를 사용한다.
     */
    @ApiErrorCode(value = {ExceptionCode.NOT_DATA, ExceptionCode.NOT_POINT})
    @PostMapping("/point/use")
    public UserResponseDto usePoint(@Valid @RequestBody final UserPointRequestDto request) {
        return userService.usePoint(request);
    }

}
