package ticket.test.ticketing;

import ticket.test.ticketing.db.Ticket;

public class TicketingResponseDto {

    public String getMassage() {
        return massage;
    }

    public Ticket getResponseTicket() {
        return responseTicket;
    }

    private final String massage;
    private final Ticket responseTicket;

    public TicketingResponseDto(
            String massage,
            Ticket responseTicket
    ) {
        this.massage = massage;
        this.responseTicket = responseTicket;
    }
}
