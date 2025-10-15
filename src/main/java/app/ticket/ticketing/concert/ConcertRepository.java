package app.ticket.ticketing.concert;

import app.ticket.ticketing.db.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {
    Concert findByConcertId(String concertId);

    @Query("SELECT concertId FROM Concert c WHERE c.concertId = :concertId AND c.isDelete = FALSE")
    Concert isExistByConcertId(@Param("concertId") String concertId);

    @Query("UPDATE Concert c SET c.isDelete = TRUE, c.deletedAt = NOW() WHERE c.concertId = :concertId")
    void deleteByConcertId(@Param("concertId") String concertId);
}