package app.ticket.Stage;

import app.ticket.StartApplication;
import app.ticket.ticketing.db.Stage;
import app.ticket.ticketing.stage.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Slf4j
@ContextConfiguration(classes = StartApplication.class)
public class StageControllerTests {

    /*
        StageController를 Test한다.
     */

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private StageController stageController;

    private List<Stage> stages;

    StageRequestDto setRequestData() {
        StageRequestDto request = new StageRequestDto();

        request.setConcertId("test");
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
        this.stages = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            Stage newData = new Stage(setRequestData());
            newData.setCreatedAt(new Date());
            this.stages.add(newData);
        }
        stageRepository.saveAllAndFlush(this.stages);
    }

    @DisplayName("stage - 생성 컨트롤러 테스트")
    @Test
    void createStageControllerTest() {
        // given
        Stage newData = new Stage(setRequestData());

        // when
        StageResponseDto result = stageController.createStage(setRequestData(newData));

        // then
        assertThat(result.getStageId().length(), is(13));
        assertThat(result.getConcertId(), is("test"));
    }

    @DisplayName("stage - 조회 컨트롤러 테스트")
    @Test
    void readStagesControllerTest() {
        // when
        List<Stage> result =  stageController.readStages("test");

        // then
        assertThat(result.size(), is(5));
        for(Stage item : result) {
            assertThat(item.getStageId().length(), is(13));
        }
    }

    @DisplayName("stage - 수정 컨트롤러 테스트")
    @Test
    void updateStageControllerTest() {
        // given
        List<Stage> testDatas =  this.stages;
        Stage testData = testDatas.get(0);

        // when
        StageRequestDto data = setRequestData(testData);
        data.setStageTime(new Date());
        StageResponseDto result =  stageController.updateStage(data);

        // then
        assertThat(result.getStageId(), is(notNullValue()));
    }

    @DisplayName("stage - 삭제 컨트롤러 테스트")
    @Test
    void deleteStageControllerTest() {
        // given
        List<Stage> testDatas =  this.stages;
        Stage testData = testDatas.get(0);

        // when
        StageRequestDto data = setRequestData(testData);
        stageController.deleteStage(data);
        Stage result = stageRepository.findByStageId(testData.getStageId());

        // then
        assertThat(result, is(nullValue()));
    }

    @AfterEach()
    void deleteData() {
        for(Stage item : stageRepository.findByConcertId("test")) {
            stageRepository.delete(item);
        }
    }
}
