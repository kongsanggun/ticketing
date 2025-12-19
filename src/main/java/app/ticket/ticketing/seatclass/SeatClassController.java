package app.ticket.ticketing.seatclass;

import app.ticket.ticketing.common.exception.ApiErrorCode;
import app.ticket.ticketing.common.exception.ExceptionCode;
import app.ticket.ticketing.db.SeatClass;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Seat class API", description = "공연 내 좌석을 관리해주는 API입니다.")
@RequiredArgsConstructor
@RestController
public class SeatClassController {
    private final SeatClassService seatClassService;

    @ApiErrorCode(value = {ExceptionCode.NOT_DATA})
    @GetMapping("/seat-class/{id}")
    public List<SeatClass> readSeatClass(@PathVariable final String id) {
        return seatClassService.readSeatClass(id);
    }

    @ApiErrorCode(value = {ExceptionCode.NOT_DATA})
    @PostMapping("/seat-class")
    @ResponseStatus(HttpStatus.CREATED)
    public SeatClassResponseDto createSeatClass(@Valid @RequestBody final SeatClassRequestDto request) {
        return seatClassService.createSeatClass(request);
    }

    @ApiErrorCode(value = {ExceptionCode.NOT_DATA})
    @PutMapping("/seat-class")
    public SeatClassResponseDto updateSeatClass(@RequestBody final SeatClassRequestDto request) {
        return seatClassService.updateSeatClass(request);
    }

    @ApiErrorCode(value = {ExceptionCode.NOT_DATA, ExceptionCode.EMPTY_PRICE})
    @DeleteMapping("/seat-class")
    public void deleteSeatClass(@RequestBody final SeatClassRequestDto request) {
        seatClassService.deleteSeatClass(request);
    }
}
