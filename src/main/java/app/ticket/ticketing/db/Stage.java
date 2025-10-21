package app.ticket.ticketing.db;

import app.ticket.ticketing.concert.ConcertRequestDto;
import app.ticket.ticketing.stage.StageRequestDto;

import io.hypersistence.tsid.TSID;
import io.hypersistence.utils.hibernate.id.Tsid;
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

    // TSID 전략으로 PK 키 생성
    @Id
    @Column(name="stageId", columnDefinition = "CHAR(13)")
    private String stageId;

    @Column(nullable = false, name="concertId")
    private String concertId;

    @Column(nullable = false, name="stageTime")
    private Date stageTime;

    public Stage(StageRequestDto request) {
        this.stageId = TSID.fast().toString();
        this.concertId = request.getConcertId();
        this.stageTime = request.getStageTime();
    }

    public Stage(ConcertRequestDto request) {
        this.stageId = TSID.fast().toString();
        this.concertId = request.getConcertId();
        this.stageTime = request.getStageTime();
    }
}
