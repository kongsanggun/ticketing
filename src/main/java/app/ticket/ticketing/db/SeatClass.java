package app.ticket.ticketing.db;

import app.ticket.ticketing.concert.ConcertRequestDto;
import app.ticket.ticketing.seatclass.SeatClassRequestDto;
import io.hypersistence.tsid.TSID;
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
@Table(name = "seat_class")
public class SeatClass extends Basedb {

    // TSID 전략으로 PK 키 생성
    @Id
    @Column(name = "seatClassId", columnDefinition = "CHAR(13)")
    private String seatClassId;

    @Column(nullable = false, name = "concertId")
    private String concertId;

    @Column(name = "name")
    private String name;

    @Column(nullable = false, name = "price")
    private Integer price;

    public SeatClass(SeatClassRequestDto request) {
        this.seatClassId = TSID.fast().toString();
        this.concertId = request.getConcertId();
        this.name = request.getName();
        this.price = request.getPrice();
    }

    public SeatClass(ConcertRequestDto request) {
        this.seatClassId = TSID.fast().toString();
        this.concertId = request.getConcertId();
        this.name = request.getPriceName();
        this.price = request.getPrice();
    }
}
