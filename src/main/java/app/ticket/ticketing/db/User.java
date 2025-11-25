package app.ticket.ticketing.db;

import app.ticket.ticketing.user.UserRequestDto;
import io.hypersistence.tsid.TSID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User extends Basedb {

    // TSID 전략으로 PK 키 생성
    @Id
    @Column(name = "userId", columnDefinition = "CHAR(13)")
    private String userId;

    @Column(name = "name")
    private String name;

    @Column(name = "point")
    private int point;

    public User(UserRequestDto request) {
        this.userId = TSID.fast().toString();
        this.name = request.getName();
        this.point = request.getPoint();
    }
}
