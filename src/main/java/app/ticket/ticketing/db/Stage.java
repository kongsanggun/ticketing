package app.ticket.ticketing.db;

import app.ticket.ticketing.concert.ConcertRequestDto;
import app.ticket.ticketing.stage.StageRequestDto;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stage")
public class Stage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="stageId")
    private Long stageId;

    @Column(unique = true, nullable = false, name="concertId")
    private String concertId;

    @Column(nullable = false, name="stageTime")
    private Date stageTime;

    public Stage(StageRequestDto request) {
        this.concertId = request.getConcertId();
        this.stageTime = request.getStageTime();
    }

    public Stage(ConcertRequestDto request) {
        this.concertId = request.getConcertId();
        this.stageTime = request.getStageTime();
    }
}
