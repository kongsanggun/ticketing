package app.ticket.ticketing.db;

import app.ticket.ticketing.seatclass.SeatClassRequestDto;
import app.ticket.ticketing.concert.ConcertRequestDto;
import io.hypersistence.utils.hibernate.id.Tsid;
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

    // TSID 전략으로 PK 키 생성
    @Id
    @Tsid
    @Column(name="seatClassId", columnDefinition = "CHAR(13)")
    private String seatClassId;

    @Column(unique = true, nullable = false, name="concertId")
    private String concertId;

    @Column(name="name")
    private String name;

    @Column(nullable = false, name="price")
    private Integer price;

    public SeatClass(SeatClassRequestDto request) {
        this.concertId = request.getConcertId();
        this.name = request.getName();
        this.price = request.getPrice();
    }

    public SeatClass(ConcertRequestDto request) {
        this.concertId = request.getConcertId();
        this.name = request.getPriceName();
        this.price = request.getPrice();
    }
}
