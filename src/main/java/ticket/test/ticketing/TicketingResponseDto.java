package ticket.test.ticketing;

import ticket.test.ticketing.db.Ticket;

public class TicketingResponseDto {

    public String getMessage() {
        return message;
    }

    public Ticket getResponseTicket() {
        return responseTicket;
    }

    private final String message;
    private final Ticket responseTicket;

    public TicketingResponseDto(
            String message,
            Ticket responseTicket
    ) {
        this.message = message;
        this.responseTicket = responseTicket;
    }
}
