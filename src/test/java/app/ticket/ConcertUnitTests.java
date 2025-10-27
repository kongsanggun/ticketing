package app.ticket;

import app.ticket.ticketing.concert.ConcertRepository;
import app.ticket.ticketing.concert.ConcertRequestDto;
import app.ticket.ticketing.db.Concert;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;

import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Slf4j
public class ConcertUnitTests {

    /*
        concert 객체를 Test한다.
     */

    @Autowired
    private ConcertRepository concertRepository;

    private Concert concert;

    ConcertRequestDto setRequestData() {
        ConcertRequestDto request = new ConcertRequestDto();

        request.setName("test");
        request.setDetail("테스트 공연");
        request.setBookStartTime(new Date());
        request.setStageTime(new Date());
        request.setPriceName("S석");
        request.setPrice(39800);

        return request;
    }

    @BeforeEach()
    void setData() {
        this.concert = new Concert(setRequestData());
        this.concert.setCreatedAt(new Date());

        concertRepository.saveAndFlush(concert);
    }

    @DisplayName("concert - 생성 테스트")
    @Test
    void createConcertTest() {
        // given
        Concert newData = new Concert(setRequestData());
        newData.setCreatedAt(new Date());

        // when
        Concert result = concertRepository.saveAndFlush(newData);

        // then
        assertThat(result.getConcertId().length(), is(13));
        assertThat(result.getName(), is("test"));

        concertRepository.delete(newData);
    }

    @DisplayName("concert - 잘못 된 값으로 생성된 테스트")
    @Test
    void createWrongConcertTest() {
        // given
        Concert notCreatedAtData = new Concert(setRequestData());
        Concert notIdData = new Concert();

        // when

        // then
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            concertRepository.saveAndFlush(notCreatedAtData);
        });
        Assertions.assertThrows(JpaSystemException.class, () -> {
            concertRepository.saveAndFlush(notIdData);
        });
    }

    @DisplayName("concert - 단건 조회 테스트")
    @Test
    void readConcertTest() {
        // given
        Concert testData= this.concert;

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
        newData.setCreatedAt(new Date());
        concertRepository.saveAndFlush(newData);

        // when
        List<Concert> result = concertRepository.findByName("test");

        // then
        assertThat(result.size(), not(1));
        for(Concert item : result) {
            assertThat(item.getConcertId().length(), is(13));
        }

        concertRepository.delete(newData);
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
        Concert testData= this.concert;

        // when
        testData.setName("testUpdated");
        testData.setUpdatedAt(new Date());
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
        Assertions.assertThrows(NullPointerException.class, () -> {
            result.getConcertId();
        });
    }

    @AfterEach()
    void deleteData() {
        concertRepository.delete(this.concert);
    }
}
