package app.ticket.ticketing.db;

import app.ticket.ticketing.ticketing.TicketingRequestDto;
import io.hypersistence.tsid.TSID;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table (
        name = "ticket",
        indexes = @Index(
                name = "concert_stage_seat_class_seat_ux",
                columnList = "concertId, stageId, seatClassId, seat",
                unique = true
        )
)
public class Ticket extends Basedb {
    @Id
    @PrimaryKeyJoinColumn
    @Column(name = "ticketId", columnDefinition = "CHAR(13)")
    private String ticketId;

    @NotNull()
    @Column(name = "concertId")
    private String concertId;

    @NotNull()
    @Column(name = "stageId")
    private String stageId;

    @NotNull()
    @Column(name = "seatClassId")
    private String seatClassId;

    @NotNull()
    @Column(name = "seat")
    private int seat;

    @NotNull()
    @Column(name = "userId")
    private String userId;

    public Ticket(TicketingRequestDto dto, int seat) {
        this.ticketId = TSID.fast().toString();
        this.concertId = dto.getConcertId();
        this.stageId = dto.getStageId();
        this.seatClassId = dto.getSeatClassId();
        this.userId = dto.getUserId();
        this.seat = seat;
    }
}
