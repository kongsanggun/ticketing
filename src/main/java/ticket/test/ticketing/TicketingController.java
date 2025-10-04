package ticket.test.ticketing;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ticket.test.ticketing.db.Ticket;

@RequiredArgsConstructor
@RestController
public class TicketingController {
    private final TicketingService ticketingService;

    @PostMapping("/ticket")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketingResponseDto createTicket(@RequestBody final TicketingRequestDto request) throws Exception {
        return ticketingService.createTicket(request);
    }

    @DeleteMapping("/cancel")
    public void cancelTicket(@RequestBody final TicketingRequestDto request) {
        ticketingService.cancelTicket(request);
    }

    @GetMapping("/check/{id}")
    public Ticket checkTicket(@PathVariable final String id) {
        return ticketingService.checkTicket(id);
    }
}
