package app.ticket.SeatClass;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import app.ticket.StartApplication;
import app.ticket.ticketing.db.SeatClass;
import app.ticket.ticketing.seatclass.SeatClassRepository;
import app.ticket.ticketing.seatclass.SeatClassRequestDto;
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
public class SeatClassUnitTests {

    /*
     * SeatClass 객체를 Test한다.
     */

    @Autowired
    private SeatClassRepository seatClassRepository;

    private List<SeatClass> seatClass;

    SeatClassRequestDto setRequestData() {
        char seatChar = (char) (Math.round((Math.random() * 14) + 65));
        return new SeatClassRequestDto(
                "test",
                "test",
                seatChar + "석",
                (int) (Math.random() * 30000),
                100
        );
    }

    @BeforeEach()
    void setData() {
        this.seatClass = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SeatClass newData = new SeatClass(setRequestData());
            this.seatClass.add(newData);
        }
        seatClassRepository.saveAllAndFlush(this.seatClass);
    }

    @DisplayName("seatClass - 생성 테스트")
    @Test
    void createSeatClassTest() {
        // given
        SeatClass newData = new SeatClass(setRequestData());

        // when
        SeatClass result = seatClassRepository.saveAndFlush(newData);

        // then
        assertThat(result.getSeatClassId().length(), is(13));
        assertThat(result.getSeatClassId(), is(newData.getSeatClassId()));
        assertThat(result.getConcertId(), is("test"));

        seatClassRepository.delete(newData);
    }

    @DisplayName("seatClass - 잘못 된 값으로 생성된 테스트")
    @Test
    void createWrongSeatClassTest() {
        // given
        SeatClass notIdData = new SeatClass();

        // when

        // then
        Assertions.assertThrows(JpaSystemException.class, () -> {
            seatClassRepository.saveAndFlush(notIdData);
        });
    }

    @DisplayName("seatClass - 단건 조회 테스트")
    @Test
    void readSeatClassTest() {
        // given
        List<SeatClass> testDatas = this.seatClass;
        SeatClass testData = testDatas.get(0);

        // when
        SeatClass result = seatClassRepository.findBySeatClassId(testData.getSeatClassId());

        // then
        assertThat(result.getSeatClassId().length(), is(13));
        assertThat(result.getSeatClassId(), is(testData.getSeatClassId()));
    }

    @DisplayName("seatClass - 다건 조회 테스트")
    @Test
    void readSeatClassesTest() {
        // given

        // when
        List<SeatClass> result = seatClassRepository.findByConcertId("test");

        // then
        assertThat(result.size(), is(5));
        for (SeatClass item : result) {
            assertThat(item.getSeatClassId().length(), is(13));
        }
    }

    @DisplayName("seatClass - 존재하지 않는 공연 조회 테스트")
    @Test
    void readWrongSeatClassTest() {
        // given
        SeatClass testData = new SeatClass();

        // when
        List<SeatClass> resultList = seatClassRepository.findByConcertId(testData.getConcertId());
        SeatClass result = seatClassRepository.findBySeatClassId(testData.getSeatClassId());

        // then
        Assertions.assertThrows(NullPointerException.class, () -> {
            assertThat(result.getSeatClassId().length(), is(13));
            assertThat(result.getSeatClassId(), is(testData.getSeatClassId()));
        });

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            assertThat(resultList.get(0).getSeatClassId().length(), is(13));
            assertThat(resultList.get(0).getSeatClassId(), is(testData.getSeatClassId()));
        });
    }

    @DisplayName("seatClass - 수정 테스트")
    @Test
    void updateSeatClassTest() {
        // given
        List<SeatClass> testDatas = this.seatClass;
        SeatClass testData = testDatas.get(0);

        // when
        testData.putData(new SeatClassRequestDto(
                "test",
                "test",
                "testUpdated",
                10000,
                100
        ));
        SeatClass result = seatClassRepository.saveAndFlush(testData);

        // then
        assertThat(result.getSeatClassId(), is(testData.getSeatClassId()));
        assertThat(result.getPrice(), is(10000));
        assertThat(result.getName(), is("testUpdated"));
        assertThat(result.getUpdatedAt(), is(notNullValue()));
    }

    @DisplayName("seatClass - 삭제 테스트")
    @Test
    void deleteSeatClassTest() {
        // given
        List<SeatClass> testDatas = this.seatClass;
        SeatClass testData = testDatas.get(0);

        // when
        seatClassRepository.delete(testData);
        SeatClass result = seatClassRepository.findBySeatClassId(testData.getSeatClassId());

        // then
        assertThat(result, is(nullValue()));
    }

    @AfterEach()
    void deleteData() {
        seatClassRepository.deleteAll();
    }
}
