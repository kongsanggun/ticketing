package app.ticket.ticketing.db;

import app.ticket.ticketing.concert.ConcertRequestDto;
import io.hypersistence.tsid.TSID;
import jakarta.persistence.*;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "concert")
public class Concert extends Basedb {

    // TSID 전략으로 PK 키 생성
    @Id
    @PrimaryKeyJoinColumn
    @Column(name = "concertId", columnDefinition = "CHAR(13)")
    private String concertId;

    @Column(name = "name")
    private String name;

    @Column(name = "detail")
    private String detail;

    @Column(name = "bookStartTime")
    private Date bookStartTime;

    public Concert(ConcertRequestDto request) {
        this.concertId = TSID.fast().toString();
        this.name = request.getName();
        this.detail = request.getDetail();
        this.bookStartTime = request.getBookStartTime();
    }
}
