package app.ticket.ticketing.ticketing;

import app.ticket.ticketing.db.Ticket;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class TicketingController {
    private final TicketingService ticketingService;

    @PostMapping("/ticket/{seat}")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketingResponseDto createSeatedTicket(
            @PathVariable final int seat,
            @Valid @RequestBody final TicketingRequestDto request) {
        return ticketingService.createSeatedTicket(seat, request);
    }

    @PostMapping("/ticket/random")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketingResponseDto createRandomTicket(@Valid @RequestBody final TicketingRequestDto request) {
        return ticketingService.createRandomTicket(request);
    }

    @DeleteMapping("/ticket")
    public void cancelTicket(@RequestBody final TicketingRequestDto request) {
        ticketingService.cancelTicket(request);
    }

    @GetMapping("/ticket/{id}")
    public Ticket checkTicket(@PathVariable final String id) {
        return ticketingService.checkTicket(id);
    }
}
