package app.ticket.ticketing.db;

import app.ticket.ticketing.price.PriceRequestDto;
import app.ticket.ticketing.concert.ConcertRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "price")
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="priceId")
    private Long priceId;

    @Column(unique = true, nullable = false, name="concertId")
    private String concertId;

    @Column(name="name")
    private String name;

    @Column(nullable = false, name="price")
    private Integer price;

    public Price(PriceRequestDto request) {
        this.concertId = request.getConcertId();
        this.name = request.getName();
        this.price = request.getPrice();
    }

    public Price(ConcertRequestDto request) {
        this.concertId = request.getConcertId();
        this.name = request.getPriceName();
        this.price = request.getPrice();
    }
}
