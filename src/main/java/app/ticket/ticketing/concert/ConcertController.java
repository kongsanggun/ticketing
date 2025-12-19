package app.ticket.ticketing.concert;

import app.ticket.ticketing.common.exception.ApiErrorCode;
import app.ticket.ticketing.common.exception.ExceptionCode;
import app.ticket.ticketing.db.Concert;
import app.ticket.ticketing.seatclass.SeatClassService;
import app.ticket.ticketing.stage.StageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Concert API", description = "공연을 관리해주는 API입니다.")
@RequiredArgsConstructor
@RestController
public class ConcertController {
    private final ConcertService concertService;
    private final SeatClassService seatClassService;
    private final StageService stageService;

    @ApiErrorCode(value = {ExceptionCode.NOT_DATA})
    @GetMapping("/concert/{id}")
    public Concert readConcert(@PathVariable final String id) {
        return concertService.readConcert(id);
    }

    @ApiErrorCode(value = {ExceptionCode.ADDED_SHOW, ExceptionCode.NOT_DATA})
    @PostMapping("/concert")
    @ResponseStatus(HttpStatus.CREATED)
    public ConcertResponseDto createConcert(@Valid @RequestBody final ConcertRequestDto request) {
        ConcertResponseDto result = concertService.createConcert(request);
        seatClassService.createSeatClassByConcert(result.getConcertId(), request);
        stageService.createPriceByConcert(result.getConcertId(), request);

        return result;
    }

    @ApiErrorCode(value = {ExceptionCode.ADDED_SHOW})
    @PutMapping("/concert")
    public ConcertResponseDto updateConcert(@RequestBody final ConcertRequestDto request) {
        return concertService.updateConcert(request);
    }

    @ApiErrorCode(value = {ExceptionCode.ADDED_SHOW})
    @DeleteMapping("/concert")
    public void deleteConcert(@RequestBody final ConcertRequestDto request) {
        concertService.deleteConcert(request);
    }
}
