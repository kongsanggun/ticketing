package app.ticket.ticketing.seatclass;

import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private String concertId;
    private String name;
    @NotNull
    private Integer price;
    @NotNull
    private Integer capacity;
}
