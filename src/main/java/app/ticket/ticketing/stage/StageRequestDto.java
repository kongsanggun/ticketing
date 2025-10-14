package app.ticket.ticketing.stage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StageRequestDto {
    private Long stageId;
    private String concertId;
    private Date stageTime;
}
