package app.ticket.ticketing.concert;

import app.ticket.ticketing.db.Concert;
import app.ticket.ticketing.seatclass.SeatClassService;
import app.ticket.ticketing.stage.StageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ConcertController {
    private final ConcertService concertService;
    private final SeatClassService seatClassService;
    private final StageService stageService;

    @GetMapping("/concert/{id}")
    public Concert readConcert(@PathVariable final String id) {
        return concertService.readConcert(id);
    }

    @PostMapping("/concert")
    @ResponseStatus(HttpStatus.CREATED)
    public ConcertResponseDto createConcert(@Valid @RequestBody final ConcertRequestDto request) {
        ConcertResponseDto result =  concertService.createConcert(request);

        request.setConcertId(result.getConcertId());
        seatClassService.createSeatClassByConcert(request);
        stageService.createPriceByConcert(request);

        return result;
    }

    @PutMapping("/concert")
    public ConcertResponseDto updateConcert(@RequestBody final ConcertRequestDto request) {
        return concertService.updateConcert(request);
    }

    @DeleteMapping("/concert")
    public void deleteConcert(@RequestBody final ConcertRequestDto request) {
        concertService.deleteConcert(request);
    }
}
