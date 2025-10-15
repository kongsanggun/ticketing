package app.ticket.ticketing.seatclass;

import app.ticket.ticketing.db.SeatClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SeatClassResponseDto {
    private String seatClassId;
    private String concertId;
    private String name;
    private Integer price;

    public SeatClassResponseDto(SeatClass seatClass) {
        this.seatClassId = seatClass.getSeatClassId();
        this.concertId = seatClass.getConcertId();
        this.name = seatClass.getName();
        this.price = seatClass.getPrice();
    }
}
