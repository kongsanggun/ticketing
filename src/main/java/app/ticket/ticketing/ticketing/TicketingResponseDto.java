package app.ticket.ticketing.ticketing;

import app.ticket.ticketing.db.Ticket;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TicketingResponseDto {
    private String ticketId;
    private String userId;
    private String showId;
    private String seat;
    private Date bookTime;

    public TicketingResponseDto(Ticket ticket) {
        this.ticketId = ticket.getTicketId();
        this.userId = ticket.getUserId();
        this.showId = ticket.getShowId();
        this.seat = ticket.getSeat();
        this.bookTime = ticket.getBookTime();
    }
}
