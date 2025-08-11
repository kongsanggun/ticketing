package ticket.test.ticketing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import ticket.test.ticketing.db.Ticket;

@RequiredArgsConstructor
@Slf4j
@RestController
public class TicketingController {

    @Autowired
    private TicketingService ticketingService;

    @PostMapping("/ticket")
    public TicketingResponseDto getTicket(@RequestBody final TicketingRequestDto request) throws Exception {
        return ticketingService.getTicket(request);
    }

    @PostMapping("/cancel")
    public TicketingResponseDto cancelTicket(@RequestBody final TicketingRequestDto request) {
        return ticketingService.cancelTicket(request);
    }

    @GetMapping("/check/{id}")
    public Ticket checkTicket(@PathVariable final String id) {
        return ticketingService.checkTicket(id);
    }
}
