package app.ticket.ticketing.stage;

import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StageRequestDto {
    private String stageId;
    private String concertId;
    @NotNull
    private Date stageTime;
}
