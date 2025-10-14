package app.ticket.ticketing.concert;

import app.ticket.ticketing.db.Concert;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ConcertController {
    private final ConcertService concertService;

    @GetMapping("/concert/{id}")
    public Concert readConcert(@PathVariable final String id) {
        return concertService.readConcert(id);
    }

    @PostMapping("/concert")
    @ResponseStatus(HttpStatus.CREATED)
    public ConcertResponseDto createConcert(@RequestBody final ConcertRequestDto request) {
        return concertService.createConcert(request);
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
