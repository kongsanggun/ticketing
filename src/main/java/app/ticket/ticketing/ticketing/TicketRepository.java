package app.ticket.ticketing.ticketing;

import app.ticket.ticketing.db.Ticket;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query(value = """
            WITH RECURSIVE seat AS (
                SELECT 1 AS LEVEL
                UNION ALL
                SELECT 1 + seat.LEVEL
                  FROM seat
                 WHERE seat.LEVEL < (
                    select capacity
                from seat_class sc
                where sc.seat_class_id = :seatClassId
                 )
            )
            select LEVEL as seat
            from seat left join ticket t
            on t.seat = seat.LEVEL
            and t.concert_id = :concertId
            and t.stage_id = :stageId
            and t.seat_class_id = :seatClassId
            where t.seat is NULL;""", nativeQuery = true)
    List<Integer> findAvailableSeat(@Param("concertId") String concertId, @Param("stageId") String stageId, @Param("seatClassId") String seatClassId);

    @Query("SELECT ticketId FROM Ticket t WHERE t.ticketId = :ticketId")
    Optional<Ticket> findByTicketIdForUpdate(@Param("ticketId") String ticketId);

    Ticket findByTicketId(String ticketId);

    void deleteByTicketId(String ticketId);
}
