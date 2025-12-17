package app.ticket.ticketing.stage;

import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StageRequestDto {
    private String stageId;
    @NotNull
    private String concertId;
    @NotNull
    private Date stageTime;
}
