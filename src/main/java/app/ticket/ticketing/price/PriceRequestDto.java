package app.ticket.ticketing.price;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PriceRequestDto {
    private Long priceId;
    private String concertId;
    private String name;
    private Integer price;
}
