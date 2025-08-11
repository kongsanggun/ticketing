package ticket.test.ticketing.db;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ticketId FROM Ticket t WHERE t.showId = :showId AND t.seat = :seat")
    Optional<Ticket> findByShowIdAndSeatForUpdate(@Param("showId") String showId, @Param("seat") String seat);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ticketId FROM Ticket t WHERE t.ticketId = :ticketId")
    Optional<Ticket> findByTicketIdForUpdate(@Param("ticketId") String ticketId);

    @Lock(LockModeType.PESSIMISTIC_READ)
    Ticket findByUserIdAndShowId(String userId, String showId);

    Ticket findByTicketId(String ticketId);
    void deleteByTicketId(String ticketId);
}
