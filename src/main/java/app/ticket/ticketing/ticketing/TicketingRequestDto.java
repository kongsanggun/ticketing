package app.ticket.ticketing.ticketing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TicketingRequestDto {
    private String ticketId;
    private String concertId;
    private String stageId;
    private String seatClassId;
    private String userId;
}
