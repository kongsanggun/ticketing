package app.ticket.ticketing.stage;

import app.ticket.ticketing.db.Stage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StageRepository extends JpaRepository<Stage, Long> {
    List<Stage> findByConcertId(String concertId);

    Stage findByStageId(String stageId);

    void deleteByStageId(String stageId);
}
