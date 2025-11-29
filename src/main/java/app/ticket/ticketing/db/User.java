package app.ticket.ticketing.db;

import app.ticket.ticketing.user.UserCreateRequestDto;
import app.ticket.ticketing.user.UserRequestDto;
import io.hypersistence.tsid.TSID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
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

    @Column(name = "isDelete")
    private Boolean isDelete;

    public User(UserCreateRequestDto request) {
        this.userId = TSID.fast().toString();
        this.name = request.getName();
        this.point = 0;
        this.isDelete = false;
    }

    public void setDeleteData() {
        this.setDeletedAt(new Date());
        this.isDelete = true;
    }
}
