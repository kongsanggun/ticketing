package app.ticket.Concert;

import app.ticket.StartApplication;
import app.ticket.ticketing.concert.ConcertController;
import app.ticket.ticketing.concert.ConcertRepository;
import app.ticket.ticketing.concert.ConcertRequestDto;
import app.ticket.ticketing.concert.ConcertResponseDto;
import app.ticket.ticketing.db.Concert;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Slf4j
@ContextConfiguration(classes = StartApplication.class)
public class ConcertControllerTests {

    /*
        ConcertController를 Test한다.
     */

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ConcertController concertController;

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

    ConcertRequestDto setRequestData(Concert concert) {
        ConcertRequestDto request = new ConcertRequestDto();
        request.setConcertId(concert.getConcertId());
        request.setName(concert.getName());
        request.setDetail(concert.getDetail());
        request.setBookStartTime(concert.getBookStartTime());
        return request;
    }

    @BeforeEach()
    void setData() {
        this.concert = new Concert(setRequestData());
        this.concert.setCreatedAt(new Date());

        concertRepository.saveAndFlush(concert);
    }

    @DisplayName("concert - 조회 컨트롤러 테스트")
    @Test
    void readConcertControllerTest() {
        // given
        Concert testData= this.concert;

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
        data.setName("testUpdated");

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
