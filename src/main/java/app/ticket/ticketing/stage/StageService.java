package app.ticket.ticketing.stage;

import app.ticket.ticketing.common.exception.custom.stage.StageIdNotDataException;
import app.ticket.ticketing.common.exception.custom.stage.StageNotRemainException;
import app.ticket.ticketing.concert.ConcertRequestDto;
import app.ticket.ticketing.db.Stage;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional()
public class StageService {
    private final StageRepository stageRepository;

    /*
     * 공연 내 모든 시간표을 조회한다.
     */
    public List<Stage> readStages(String concertId) {
        List<Stage> stage = stageRepository.findByConcertId(concertId);
        if (stage.isEmpty()) {
            throw new StageIdNotDataException(concertId);
        }
        return stage;
    }

    /*
     * 공연 내 시간표를 추가한다.
     */
    public StageResponseDto createStage(StageRequestDto request) {
        Stage stage = new Stage(request);
        stageRepository.saveAndFlush(stage);
        return new StageResponseDto(stage);
    }

    /*
     * 공연 생성으로 인하여 공연 내 시간표를 추가한다.
     */
    public void createPriceByConcert(ConcertRequestDto request) {
        Stage stage = new Stage(request);
        stageRepository.saveAndFlush(stage);
    }

    /*
     * 공연 내 시간표를 수정한다.
     */
    public StageResponseDto updateStage(StageRequestDto request) {
        Stage stage = checkExist(request);
        stage.setStageTime(request.getStageTime());
        stageRepository.saveAndFlush(stage);
        return new StageResponseDto(stage);
    }

    /*
     * 공연 내 시간표를 삭제한다.
     */
    public void deleteStage(StageRequestDto request) {
        // 1. 삭제 이후 남아있는 가격이 존재하지 않을 경우가 있는지 확인한다.
        if (stageRepository.findByConcertIdAndIsDelete(
                request.getConcertId(),
                false).size() <= 1) {
            throw new StageNotRemainException();
        }

        // 2. 동일한 중복요청이 있는지 확인한다.
        Stage stage = checkExist(request);
        stage.setDeleteData();
        stageRepository.saveAndFlush(stage);
    }

    /*
     * 존재하는 공연 내 시간표인지 확인한다. 존재 시 해당 값을 반환한다.
     */
    private Stage checkExist(StageRequestDto request) {
        Stage stage = stageRepository.findByStageIdAndIsDelete(
                request.getStageId(),
                false
        );
        if (stage == null) {
            throw new StageIdNotDataException(request.getStageId());
        }
        return stage;
    }
}
