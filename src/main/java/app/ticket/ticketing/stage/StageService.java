package app.ticket.ticketing.stage;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;
import app.ticket.ticketing.db.Stage;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional()
public class StageService {
    private final StageRepository stageRepository;

    /*
     *  공연 내 모든 시간표을 조회한다.
     */
    public List<Stage> readStages(String concertId) {
        List<Stage> stage =  stageRepository.findByConcertId(concertId);
        if (stage == null) {
            throw new CustomException(ExceptionCode.NOT_DATA);
        }
        return stage;
    }

    /*
     *   공연 내 시간표를 추가한다.
     */
    public StageResponseDto createStage(StageRequestDto request) {
        Stage stage = new Stage(request);
        stageRepository.saveAndFlush(stage);
        return new StageResponseDto(stage);
    }

    /*
     *   공연 내 시간표를 수정한다.
     */
    public StageResponseDto updateStage(StageRequestDto request) {
        Stage stage = new Stage(request);
        stageRepository.saveAndFlush(stage);
        return new StageResponseDto(stage);
    }

    /*
     *  공연 내 시간표를 삭제한다.
     */
    public void deleteStage(StageRequestDto request) {
        // 1. 삭제 이후 남아있는 가격이 존재하지 않을 경우가 있는지 확인한다.
        if (readStages(request.getConcertId()).size() < 1) {
            throw new CustomException(ExceptionCode.EMPTY_STAGE);
        }

        // 2. 동일한 중복요청이 있는지 확인한다.
        Stage price =  stageRepository.findByStageIdAndConcertId(request.getStageId(), request.getConcertId());
        if (price == null) {
            throw new CustomException(ExceptionCode.NOT_DATA);
        }

        stageRepository.deleteByStageIdAndConcertId(request.getStageId(), request.getConcertId());
    }
}
