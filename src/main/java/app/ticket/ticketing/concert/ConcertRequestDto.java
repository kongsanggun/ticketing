package app.ticket.ticketing.concert;

import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConcertRequestDto {
    private String concertId;
    private String name;
    private String detail;
    private Date bookStartTime;
    @NotNull
    private Date stageTime;
    private String priceName;
    @NotNull
    private Integer price;
}
