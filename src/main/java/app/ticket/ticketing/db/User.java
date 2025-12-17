package app.ticket.ticketing.db;

import app.ticket.ticketing.common.exception.custom.user.NotEnoughPointsException;
import app.ticket.ticketing.user.UserCreateRequestDto;
import app.ticket.ticketing.user.UserPutRequestDto;
import io.hypersistence.tsid.TSID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
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

    public User(UserCreateRequestDto request) {
        this.userId = TSID.fast().toString();
        this.name = request.getName();
        this.point = 0;
    }

    public void chargePoint(int point) {
        this.point = this.point + point;
    }

    public void usePoint(int point) {
        if (this.point < point) {
            throw new NotEnoughPointsException(this.userId);
        }
        this.point = this.point - point;
    }

    public void putName(UserPutRequestDto request) {
        this.name = request.getName();
    }
}
