package app.ticket.ticketing.db;

import app.ticket.ticketing.concert.ConcertRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "concert")
public class Concert {
    @Id
    @PrimaryKeyJoinColumn
    @Column(name="concertId")
    private String concertId;

    @Column(name="name")
    private String name;

    @Column(name="detail")
    private String detail;

    @Column(name="bookStartTime")
    private Date bookStartTime;

    @Column(name="isDelete")
    private Boolean isDelete;

    public Concert(ConcertRequestDto request) {
        this.concertId = request.getConcertId();
        this.name = request.getName();
        this.detail = request.getDetail();
        this.bookStartTime = request.getBookStartTime();
        this.isDelete =  false;
    }
}
