package ticket.test.ticketing.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
