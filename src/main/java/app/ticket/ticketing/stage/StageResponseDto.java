package app.ticket.ticketing.stage;

import app.ticket.ticketing.db.Stage;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StageResponseDto {
    private String stageId;
    private String concertId;
    private Date stageTime;

    public StageResponseDto(Stage stage) {
        this.stageId = stage.getStageId();
        this.concertId = stage.getConcertId();
        this.stageTime = stage.getStageTime();
    }
}
