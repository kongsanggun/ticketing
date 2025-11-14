package app.ticket.ticketing.seatclass;

import app.ticket.ticketing.db.SeatClass;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatClassRepository extends JpaRepository<SeatClass, Long> {
    List<SeatClass> findByConcertId(String concertId);

    SeatClass findBySeatClassId(String seatClassId);

    void deleteBySeatClassId(String seatClassId);
}
