package app.ticket.Concert;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import app.ticket.StartApplication;
import app.ticket.ticketing.concert.ConcertRepository;
import app.ticket.ticketing.concert.ConcertRequestDto;
import app.ticket.ticketing.db.Concert;
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
public class ConcertUnitTests {

    /*
     * concert 객체를 Test한다.
     */

    @Autowired
    private ConcertRepository concertRepository;

    private Concert concert;

    ConcertRequestDto setRequestData() {
        return new ConcertRequestDto(
                "test",
                "test",
                "테스트입니다.",
                new Date(),
                new Date(),
                "U석",
                39800,
                20
        );
    }

    ConcertRequestDto setRequestData(Concert concert) {
        return new ConcertRequestDto(
                concert.getConcertId(),
                "testUpdated",
                concert.getDetail(),
                concert.getBookStartTime(),
                new Date(),
                "U석",
                39800,
                20
        );
    }

    @BeforeEach()
    void setData() {
        this.concert = new Concert(setRequestData());

        concertRepository.saveAndFlush(concert);
    }

    @DisplayName("concert - 생성 테스트")
    @Test
    void createConcertTest() {
        // given
        Concert newData = new Concert(setRequestData());

        // when
        Concert result = concertRepository.saveAndFlush(newData);

        // then
        assertThat(result.getConcertId().length(), is(13));
        assertThat(result.getName(), is("test"));
    }

    @DisplayName("concert - 잘못 된 값으로 생성된 테스트")
    @Test
    void createWrongConcertTest() {
        // given
        Concert notIdData = new Concert();

        // when

        // then
        Assertions.assertThrows(JpaSystemException.class, () -> {
            concertRepository.saveAndFlush(notIdData);
        });
    }

    @DisplayName("concert - 단건 조회 테스트")
    @Test
    void readConcertTest() {
        // given
        Concert testData = this.concert;

        // when
        Concert result = concertRepository.findByConcertId(testData.getConcertId());

        // then
        assertThat(result.getConcertId().length(), is(13));
        assertThat(result.getConcertId(), is(testData.getConcertId()));
    }

    @DisplayName("concert - 다건 조회 테스트")
    @Test
    void readConcertsTest() {
        // given
        Concert newData = new Concert(setRequestData());
        concertRepository.saveAndFlush(newData);

        // when
        List<Concert> result = concertRepository.findByName("test");

        // then
        assertThat(result.size(), not(1));
        for (Concert item : result) {
            assertThat(item.getConcertId().length(), is(13));
        }
    }

    @DisplayName("concert - 존재하지 않는 공연 조회 테스트")
    @Test
    void readWrongConcertTest() {
        // given
        Concert testData = new Concert();

        // when
        Concert result = concertRepository.findByConcertId(testData.getConcertId());

        // then
        Assertions.assertThrows(NullPointerException.class, () -> {
            assertThat(result.getConcertId().length(), is(13));
            assertThat(result.getConcertId(), is(testData.getConcertId()));
        });
    }

    @DisplayName("concert - 수정 테스트")
    @Test
    void updateConcertTest() {
        // given
        Concert testData = this.concert;

        // when
        testData.putData(setRequestData(testData));
        Concert result = concertRepository.saveAndFlush(concert);

        // then
        assertThat(result.getConcertId(), is(testData.getConcertId()));
        assertThat(result.getName(), is("testUpdated"));
        assertThat(result.getUpdatedAt(), is(notNullValue()));
    }

    @DisplayName("concert - 삭제 테스트")
    @Test
    void deleteConcertTest() {
        // given
        Concert testData = this.concert;

        // when
        concertRepository.delete(testData);
        Concert result = concertRepository.findByConcertId(testData.getConcertId());

        // then
        assertThat(result, is(nullValue()));
    }

    @AfterEach()
    void deleteData() {
        concertRepository.deleteAll();
    }
}
