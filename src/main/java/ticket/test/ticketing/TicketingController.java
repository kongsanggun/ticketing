package ticket.test.ticketing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import ticket.test.ticketing.db.Ticket;

@RequiredArgsConstructor
@RestController
public class TicketingController {
    private TicketingService ticketingService;

    @PostMapping("/ticket")
    public TicketingResponseDto createTicket(@RequestBody final TicketingRequestDto request) throws Exception {
        return ticketingService.createTicket(request);
    }

    @DeleteMapping("/cancel")
    public TicketingResponseDto cancelTicket(@RequestBody final TicketingRequestDto request) {
        return ticketingService.cancelTicket(request);
    }

    @GetMapping("/check/{id}")
    public Ticket checkTicket(@PathVariable final String id) {
        return ticketingService.checkTicket(id);
    }
}
