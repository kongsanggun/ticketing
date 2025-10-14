package app.ticket.ticketing.price;

import app.ticket.ticketing.db.Price;
import app.ticket.ticketing.db.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PriceResponseDto {
    private Long priceId;
    private String concertId;
    private String name;
    private Integer price;

    public PriceResponseDto(Price price) {
        this.priceId = price.getPriceId();
        this.concertId = price.getConcertId();
        this.name = price.getName();
        this.price = price.getPrice();
    }
}
