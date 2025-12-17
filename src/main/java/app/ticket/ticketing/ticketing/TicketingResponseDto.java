package app.ticket.ticketing.ticketing;

import app.ticket.ticketing.db.Ticket;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TicketingResponseDto {
    private String ticketId;
    private String concertId;
    private String stageId;
    private String seatClassId;
    private String userId;
    private int seat;

    public TicketingResponseDto(Ticket ticket) {
        this.ticketId = ticket.getTicketId();
        this.concertId = ticket.getUserId();
        this.stageId = ticket.getStageId();
        this.seatClassId = ticket.getSeatClassId();
        this.userId = ticket.getUserId();
        this.seat = ticket.getSeat();
    }
}
