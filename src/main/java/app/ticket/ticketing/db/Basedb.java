package app.ticket.ticketing.db;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Basedb {

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    @ColumnDefault("CURRENT_TIMESTAMP()")
    protected LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "updated_at")
    @ColumnDefault("CURRENT_TIMESTAMP()")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "deleted_at", nullable = false)
    @ColumnDefault("'9999-12-31 23:59:59'")
    private LocalDateTime deletedAt = LocalDateTime.parse("9999-12-31T23:59:59");

    @Column(name = "isDelete")
    @ColumnDefault("false")
    private Boolean isDelete = false;

    public void setDeleteData() {
        this.setDeletedAt(LocalDateTime.now());
        this.isDelete = true;
    }
}
