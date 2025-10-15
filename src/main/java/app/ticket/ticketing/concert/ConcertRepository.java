package app.ticket.ticketing.concert;

import app.ticket.ticketing.db.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {
    Concert findByConcertId(String concertId);
    Concert findByConcertIdAndIsDelete(String concertId, Boolean isDelete);
}