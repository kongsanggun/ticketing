package app.ticket.ticketing.concert;

import app.ticket.ticketing.db.Concert;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConcertResponseDto {
    private String concertId;
    private String name;
    private String detail;
    private Date bookStartTime;

    public ConcertResponseDto(Concert concert) {
        this.concertId = concert.getConcertId();
        this.name = concert.getName();
        this.detail = concert.getDetail();
        this.bookStartTime = concert.getBookStartTime();
    }
}
