package ticket.test.ticketing.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ticket.test.ticketing.TicketingRequestDto;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ticket")
public class Ticket {
    @Id
    @PrimaryKeyJoinColumn
    @Column(name="ticketId")
    private String ticketId;

    @PrimaryKeyJoinColumn
    @Column(name="userId")
    private String userId;

    @PrimaryKeyJoinColumn
    @Column(name="showId")
    private String showId;

    @Column(name="seat")
    private String seat;

    @Column(name="bookTime")
    private Date bookTime;

    public Ticket(TicketingRequestDto request, Date date) {
        this.ticketId = request.getTicketId();
        this.userId = request.getUserId();
        this.showId = request.getShowId();
        this.seat = request.getSeat();
        this.bookTime = date;
    }
}
