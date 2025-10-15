package app.ticket.ticketing.stage;

import app.ticket.ticketing.db.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StageRepository extends JpaRepository<Stage, Long> {
    List<Stage> findByConcertId(String concertId);
    Stage findByStageId(String stageId);
    void deleteByStageId(String stageId);
}
