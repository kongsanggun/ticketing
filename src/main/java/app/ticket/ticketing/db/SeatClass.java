package app.ticket.ticketing.db;

import app.ticket.ticketing.seatclass.SeatClassRequestDto;
import app.ticket.ticketing.concert.ConcertRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seat_class")
public class SeatClass extends BaseDB {
    @Id
    @Column(name="seatClassId")
    private String seatClassId;

    @Column(unique = true, nullable = false, name="concertId")
    private String concertId;

    @Column(name="name")
    private String name;

    @Column(nullable = false, name="price")
    private Integer price;

    public SeatClass(SeatClassRequestDto request) {
        this.seatClassId = request.getSeatClassId();
        this.concertId = request.getConcertId();
        this.name = request.getName();
        this.price = request.getPrice();
    }

    public SeatClass(ConcertRequestDto request) {
        this.seatClassId = UUID.randomUUID().toString();
        this.concertId = request.getConcertId();
        this.name = request.getPriceName();
        this.price = request.getPrice();
    }
}
