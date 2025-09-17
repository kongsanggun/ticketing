package ticket.test.ticketing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TicketingRequestDto {
    private String ticketId;
    private String userId;
    private String showId;
    private String seat;
}
