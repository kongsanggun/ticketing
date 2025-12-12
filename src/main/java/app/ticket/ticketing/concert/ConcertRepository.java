package app.ticket.ticketing.concert;

import app.ticket.ticketing.db.Concert;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {
    Concert findByConcertId(String concertId);

    List<Concert> findByName(String name);

    Concert findByConcertIdAndIsDelete(String concertId, Boolean isDelete);
}
