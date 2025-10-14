package app.ticket.ticketing.ticketing;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import app.ticket.ticketing.db.Ticket;

@RequiredArgsConstructor
@RestController
public class TicketingController {
    private final TicketingService ticketingService;

    @PostMapping("/ticket")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketingResponseDto createTicket(@RequestBody final TicketingRequestDto request) throws Exception {
        return ticketingService.createTicket(request);
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
