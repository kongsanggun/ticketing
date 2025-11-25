package app.ticket.ticketing.user;

import app.ticket.ticketing.db.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponseDto {
    private String userId;
    private String name;
    private int point;

    public UserResponseDto(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.point = user.getPoint();
    }
}
