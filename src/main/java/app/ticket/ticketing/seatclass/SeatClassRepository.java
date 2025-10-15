package app.ticket.ticketing.seatclass;

import app.ticket.ticketing.db.SeatClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatClassRepository extends JpaRepository<SeatClass, Long> {
    List<SeatClass> findByConcertId(String concertId);
    SeatClass findBySeatClassId(String seatClassId);
    void deleteBySeatClassId(String seatClassId);
}
