package app.ticket;

import app.ticket.ticketing.db.Stage;
import app.ticket.ticketing.stage.StageRepository;
import app.ticket.ticketing.stage.StageRequestDto;
import app.ticket.ticketing.stage.StageResponseDto;
import app.ticket.ticketing.stage.StageService;
import io.hypersistence.tsid.TSID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@SpringBootTest
@Slf4j
public class StageUnitTests {

    /*
        StageService 내 함수들을 Test한다.
     */

    @Autowired
    private StageService stageService;

    @Autowired
    private StageRepository stageRepository;

    StageRequestDto setRequestData(String concertId) {
        StageRequestDto request = new StageRequestDto();

        request.setConcertId(concertId);
        request.setStageTime(new Date());

        return request;
    }

    @DisplayName("공연 내 시간표 생성 테스트")
    @Test
    void createStageTest() {
        // given
        StageRequestDto request = setRequestData("test");

        // when
        StageResponseDto result =  stageService.createStage(request);

        // then
        assertThat(result.getStageId().length(), is(13));
        assertThat(result.getConcertId(), is("test"));
    }

    @DisplayName("공연 내 시간표 조회 테스트")
    @Test
    void readStageTest() {
        // given
        StageRequestDto request = setRequestData("test");
        stageService.createStage(request);

        // when
        List<Stage> result =  stageService.readStages("test");

        // then
        assertThat(result.size(), not(0));
        assertThat(result.get(0).getStageId().length(), is(13));
    }

    @DisplayName("공연 내 시간표 수정 테스트")
    @Test
    void updateStageTest() {
        // given
        List<Stage> readSeatClass =  stageService.readStages("test");
        String stageId = readSeatClass.get(0).getStageId();

        // when
        StageRequestDto request = new StageRequestDto();
        request.setStageId(stageId);
        request.setStageTime(new Date());

        StageResponseDto result =  stageService.updateStage(request);

        // then
        assertThat(result.getStageId(), is(stageId));
    }

    @DisplayName("공연 내 시간표 삭제 테스트")
    @Test
    void deleteStageTest() {
        // given
        StageRequestDto dataDto = setRequestData("test");
        stageService.createStage(dataDto);
        stageService.createStage(dataDto);

        List<Stage> beforeDelete =  stageService.readStages("test");
        String stageId = beforeDelete.get(0).getStageId();

        // when
        StageRequestDto request = new StageRequestDto();
        request.setConcertId("test");
        request.setStageId(stageId);

        stageService.deleteStage(request);
        List<Stage> afterDelete =  stageService.readStages("test");

        // then
        assertThat(beforeDelete.size(), not(afterDelete.size()));
    }

    @DisplayName("존재하지 않은 공연 내 시간표 테스트")
    @Test
    void notStageDataTest() {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            // findByConcertId 같은 경우 값이 없을 경우 size가 0으로 반환
            stageRepository.findByConcertId("wrongId").get(0);
        });
        Assertions.assertThrows(NullPointerException.class, () -> {
            stageRepository.findByStageId("wrongId").getStageId();
        });
    }
}
