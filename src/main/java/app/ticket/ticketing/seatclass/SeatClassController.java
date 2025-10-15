package app.ticket.ticketing.seatclass;

import app.ticket.ticketing.db.SeatClass;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class SeatClassController {
    private final SeatClassService seatClassService;
    @GetMapping("/seat-class/{id}")
    public List<SeatClass> readSeatClass(@PathVariable final String id) {
        return seatClassService.readSeatClass(id);
    }

    @PostMapping("/seat-class")
    @ResponseStatus(HttpStatus.CREATED)
    public SeatClassResponseDto createSeatClass(SeatClassRequestDto request) {
        return seatClassService.createSeatClass(request);
    }

    @PutMapping("/seat-class")
    public SeatClassResponseDto updateSeatClass(SeatClassRequestDto request) {
        return seatClassService.updateSeatClass(request);
    }

    @DeleteMapping("/seat-class")
    public void deleteSeatClass(@RequestBody final SeatClassRequestDto request) {
        seatClassService.deleteSeatClass(request);
    }
}
