package app.ticket.ticketing.db;

import app.ticket.ticketing.concert.ConcertRequestDto;
import app.ticket.ticketing.stage.StageRequestDto;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stage")
public class Stage extends BaseDB {
    @Id
    @Column(name="stageId")
    private String stageId;

    @Column(unique = true, nullable = false, name="concertId")
    private String concertId;

    @Column(nullable = false, name="stageTime")
    private Date stageTime;

    public Stage(StageRequestDto request) {
        this.stageId = request.getStageId();
        this.concertId = request.getConcertId();
        this.stageTime = request.getStageTime();
    }

    public Stage(ConcertRequestDto request) {
        this.stageId = UUID.randomUUID().toString();
        this.concertId = request.getConcertId();
        this.stageTime = request.getStageTime();
    }
}
