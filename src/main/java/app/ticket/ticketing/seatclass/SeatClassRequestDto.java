package app.ticket.ticketing.seatclass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SeatClassRequestDto {
    private String seatClassId;
    private String concertId;
    private String name;
    private Integer price;
}
