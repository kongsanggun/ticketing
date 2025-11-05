package app.ticket.ticketing.stage;

import jakarta.validation.constraints.NotNull;
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
    private String stageId;
    private String concertId;
    @NotNull
    private Date stageTime;
}
