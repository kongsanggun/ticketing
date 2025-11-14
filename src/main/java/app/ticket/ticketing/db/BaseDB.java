package app.ticket.ticketing.db;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseDB{
    @CreatedDate
    @Column(name = "created_at", nullable = false)
    @ColumnDefault("CURRENT_TIMESTAMP()")
    protected Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    @ColumnDefault("CURRENT_TIMESTAMP()")
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;
}
