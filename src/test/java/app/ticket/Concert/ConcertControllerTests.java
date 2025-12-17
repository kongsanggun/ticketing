package app.ticket.Concert;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import app.ticket.StartApplication;
import app.ticket.ticketing.concert.ConcertController;
import app.ticket.ticketing.concert.ConcertRepository;
import app.ticket.ticketing.concert.ConcertRequestDto;
import app.ticket.ticketing.concert.ConcertResponseDto;
import app.ticket.ticketing.db.Concert;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@Slf4j
@ContextConfiguration(classes = StartApplication.class)
public class ConcertControllerTests {

    /*
     * ConcertController를 Test한다.
     */

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ConcertController concertController;

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

    @DisplayName("concert - 조회 컨트롤러 테스트")
    @Test
    void readConcertControllerTest() {
        // given
        Concert testData = this.concert;

        // when
        Concert result = concertController.readConcert(testData.getConcertId());

        // then
        assertThat(result.getConcertId().length(), is(13));
        assertThat(result.getConcertId(), is(testData.getConcertId()));
    }

    @DisplayName("concert - 수정 컨트롤러 테스트")
    @Test
    void updateConcertControllerTest() {
        // given
        Concert testData = this.concert;

        // when
        ConcertRequestDto data = setRequestData(testData);
        ConcertResponseDto result = concertController.updateConcert(data);

        // then
        assertThat(result.getConcertId(), is(testData.getConcertId()));
        assertThat(result.getName(), is("testUpdated"));
    }

    @DisplayName("concert - 삭제 컨트롤러 테스트")
    @Test
    void deleteConcertTest() {
        // given
        Concert testData = this.concert;

        // when
        concertController.deleteConcert(setRequestData(testData));
        Concert result = concertRepository.findByConcertIdAndIsDelete(testData.getConcertId(), false);

        // then
        assertThat(result, is(nullValue()));
    }

    @AfterEach()
    void deleteData() {
        concertRepository.delete(this.concert);
    }
}
