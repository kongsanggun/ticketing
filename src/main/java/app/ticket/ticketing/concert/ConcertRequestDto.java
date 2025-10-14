package app.ticket.ticketing.concert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConcertRequestDto {
    private String concertId;
    private String name;
    private String detail;
    private Date bookStartTime;
    private Date stageTime;
    private String priceName;
    private Integer price;
}
