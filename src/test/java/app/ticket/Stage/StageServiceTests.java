package app.ticket.Stage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import app.ticket.StartApplication;
import app.ticket.ticketing.common.exception.custom.stage.StageIdNotDataException;
import app.ticket.ticketing.common.exception.custom.stage.StageNotRemainException;
import app.ticket.ticketing.db.Stage;
import app.ticket.ticketing.stage.*;
import io.hypersistence.tsid.TSID;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@Slf4j
@ContextConfiguration(classes = StartApplication.class)
public class StageServiceTests {

    /*
     * StageService를 Test한다.
     */

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private StageService stageService;

    private String concertId;
    private List<Stage> stages;

    StageRequestDto setRequestData() {
        StageRequestDto request = new StageRequestDto();
        request.setConcertId(this.concertId);
        request.setStageTime(new Date());
        return request;
    }

    StageRequestDto setRequestData(Stage stage) {
        StageRequestDto request = new StageRequestDto();
        request.setStageId(stage.getStageId());
        request.setStageTime(stage.getStageTime());
        request.setConcertId(stage.getConcertId());
        return request;
    }

    @BeforeEach()
    void setData() {
        this.concertId = TSID.fast().toString();
        this.stages = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Stage newData = new Stage(setRequestData());
            this.stages.add(newData);
        }
        stageRepository.saveAllAndFlush(this.stages);
    }

    @DisplayName("stage - 조회 서비스 테스트")
    @Test
    void readStageServiceTest() {
        // when
        List<Stage> result = stageService.readStages(this.concertId);

        // then
        assertThat(result.size(), is(3));
        for (Stage item : result) {
            assertThat(item.getStageId().length(), is(13));
        }

        assertThatThrownBy(() -> stageService.readStages("wrongTest")).isInstanceOf(StageIdNotDataException.class);
    }

    @DisplayName("stage - 수정 서비스 테스트")
    @Test
    void updateStageServiceTest() {
        // given
        List<Stage> testDatas = this.stages;
        Stage testData = testDatas.get(0);

        // when
        StageRequestDto data = setRequestData(testData);
        data.setStageTime(new Date());
        StageResponseDto result = stageService.updateStage(data);

        // then
        assertThat(result.getStageId(), is(notNullValue()));
    }

    @DisplayName("stage - 삭제 서비스 테스트")
    @Test
    void deleteStageServiceTest() {
        // given
        List<Stage> testDatas = this.stages;
        Stage testData = testDatas.get(0);

        // when
        StageRequestDto data = setRequestData(testData);
        stageService.deleteStage(data);
        Stage result = stageRepository.findByStageId(testData.getStageId());

        // then
        assertThat(result.getIsDelete(), is(true));

        assertThatThrownBy(() -> stageService.deleteStage(setRequestData(testDatas.get(0))))
                        .isInstanceOf(StageIdNotDataException.class);

        assertThatThrownBy(() -> {
            stageService.deleteStage(setRequestData(testDatas.get(1)));
            stageService.deleteStage(setRequestData(testDatas.get(2)));
        }).isInstanceOf(StageNotRemainException.class);
    }

    @AfterEach()
    void deleteData() {
        stageRepository.deleteAll();
    }
}
