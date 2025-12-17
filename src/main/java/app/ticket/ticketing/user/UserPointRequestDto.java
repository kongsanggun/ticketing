package app.ticket.ticketing.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserPointRequestDto {
    @NotNull()
    private String userId;
    @Min(0)
    private int point;
}
