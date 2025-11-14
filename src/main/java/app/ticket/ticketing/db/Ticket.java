package app.ticket.ticketing.db;

import app.ticket.ticketing.ticketing.TicketingRequestDto;
import jakarta.persistence.*;
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
@Table(name = "ticket")
public class Ticket extends Basedb {
    @Id
    @PrimaryKeyJoinColumn
    @Column(name = "ticketId")
    private String ticketId;

    @PrimaryKeyJoinColumn
    @Column(name = "userId")
    private String userId;

    @PrimaryKeyJoinColumn
    @Column(name = "showId")
    private String showId;

    @Column(name = "seat")
    private String seat;

    @Column(name = "bookTime")
    private Date bookTime;

    public Ticket(TicketingRequestDto request) {
        this.ticketId = request.getTicketId();
        this.userId = request.getUserId();
        this.showId = request.getShowId();
        this.seat = request.getSeat();
        this.bookTime = new Date();
        this.createdAt = new Date();
    }
}
