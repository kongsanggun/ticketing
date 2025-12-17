package app.ticket.Stage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import app.ticket.StartApplication;
import app.ticket.ticketing.db.Stage;
import app.ticket.ticketing.stage.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@Slf4j
@ContextConfiguration(classes = StartApplication.class)
public class StageUnitTests {

    /*
     * Stage 객체를 Test한다.
     */

    @Autowired
    private StageRepository stageRepository;

    private List<Stage> stages;

    StageRequestDto setRequestData() {
        return new StageRequestDto(
                "test",
                "test",
                new Date()
        );
    }

    @BeforeEach()
    void setData() {
        this.stages = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Stage newData = new Stage(setRequestData());
            this.stages.add(newData);
        }
        stageRepository.saveAllAndFlush(this.stages);
    }

    @DisplayName("stage - 생성 테스트")
    @Test
    void createStageTest() {
        // given
        Stage newData = new Stage(setRequestData());

        // when
        Stage result = stageRepository.saveAndFlush(newData);

        // then
        assertThat(result.getStageId().length(), is(13));
        assertThat(result.getStageId(), is(newData.getStageId()));
        assertThat(result.getConcertId(), is("test"));

        stageRepository.delete(newData);
    }

    @DisplayName("stage - 잘못 된 값으로 생성된 테스트")
    @Test
    void createWrongStageTest() {
        // given
        Stage notIdData = new Stage();

        // when

        // then
        Assertions.assertThrows(JpaSystemException.class, () -> {
            stageRepository.saveAndFlush(notIdData);
        });
    }

    @DisplayName("stage - 단건 조회 테스트")
    @Test
    void readStageTest() {
        // given
        List<Stage> testDatas = this.stages;
        Stage testData = testDatas.get(0);

        // when
        Stage result = stageRepository.findByStageId(testData.getStageId());

        // then
        assertThat(result.getStageId().length(), is(13));
        assertThat(result.getStageId(), is(testData.getStageId()));
    }

    @DisplayName("stage - 다건 조회 테스트")
    @Test
    void readStagesTest() {
        // given

        // when
        List<Stage> result = stageRepository.findByConcertId("test");

        // then
        assertThat(result.size(), is(5));
        for (Stage item : result) {
            assertThat(item.getStageId().length(), is(13));
        }
    }

    @DisplayName("stage - 존재하지 않는 공연 조회 테스트")
    @Test
    void readWrongStageTest() {
        // given
        Stage testData = new Stage();

        // when
        List<Stage> resultList = stageRepository.findByConcertId(testData.getConcertId());
        Stage result = stageRepository.findByStageId(testData.getStageId());

        // then
        Assertions.assertThrows(NullPointerException.class, () -> {
            assertThat(result.getStageId().length(), is(13));
            assertThat(result.getStageId(), is(testData.getStageId()));
        });

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            assertThat(resultList.get(0).getStageId().length(), is(13));
            assertThat(resultList.get(0).getStageId(), is(testData.getStageId()));
        });
    }

    @DisplayName("stage - 수정 테스트")
    @Test
    void updateStageTest() {
        // given
        List<Stage> testDatas = this.stages;
        Stage testData = testDatas.get(0);

        // when
        Date updateDate = new Date();
        testData.putData(setRequestData());
        Stage result = stageRepository.saveAndFlush(testData);

        // then
        assertThat(result.getUpdatedAt(), is(notNullValue()));
    }

    @DisplayName("stage - 삭제 테스트")
    @Test
    void deleteStageTest() {
        // given
        List<Stage> testDatas = this.stages;
        Stage testData = testDatas.get(0);

        // when
        stageRepository.delete(testData);
        Stage result = stageRepository.findByStageId(testData.getStageId());

        // then
        assertThat(result, is(nullValue()));
    }

    @AfterEach()
    void deleteData() {
        stageRepository.deleteAll();
    }
}
