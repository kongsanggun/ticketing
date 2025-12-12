package app.ticket.Concert;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import app.ticket.StartApplication;
import app.ticket.ticketing.common.exception.custom.concert.ConcertAlreadyExistException;
import app.ticket.ticketing.common.exception.custom.concert.ConcertIdNotDataException;
import app.ticket.ticketing.concert.ConcertRepository;
import app.ticket.ticketing.concert.ConcertRequestDto;
import app.ticket.ticketing.concert.ConcertResponseDto;
import app.ticket.ticketing.concert.ConcertService;
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
public class ConcertServiceTests {

    /*
     * concert 객체를 Test한다.
     */

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ConcertService concertService;

    private Concert concert;

    ConcertRequestDto setRequestData() {
        ConcertRequestDto request = new ConcertRequestDto();

        request.setName("test");
        request.setDetail("테스트 공연");
        request.setBookStartTime(new Date());
        request.setStageTime(new Date());
        request.setPriceName("S석");
        request.setInitialPrice(39800);

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
        concertRepository.saveAndFlush(concert);
    }

    @DisplayName("concert - 생성 서비스 테스트")
    @Test
    void createConcertServiceTest() {
        // given
        Concert newData = new Concert(setRequestData());

        // when
        ConcertResponseDto result = concertService.createConcert(setRequestData(newData));
        newData.setConcertId(result.getConcertId());

        // then
        assertThat(result.getConcertId().length(), is(13));
        assertThat(result.getName(), is("test"));
        assertThatThrownBy(() -> concertService.createConcert(setRequestData(newData)))
                        .isInstanceOf(ConcertAlreadyExistException.class);
    }

    @DisplayName("concert - 조회 서비스 테스트")
    @Test
    void readConcertServiceTest() {
        // when
        Concert result = concertService.readConcert(concert.getConcertId());

        // then
        assertThat(result.getConcertId().length(), is(13));
        assertThatThrownBy(() -> concertService.readConcert("wrongTest")).isInstanceOf(ConcertIdNotDataException.class);
    }

    @DisplayName("concert - 수정 서비스 테스트")
    @Test
    void updateConcertServiceTest() {
        // given
        Concert testData = this.concert;

        // when
        ConcertRequestDto data = setRequestData(testData);
        data.setName("testUpdated");
        ConcertResponseDto result = concertService.updateConcert(data);

        // then
        assertThat(result.getConcertId(), is(testData.getConcertId()));
        assertThat(result.getName(), is("testUpdated"));
    }

    @DisplayName("concert - 삭제 서비스 테스트")
    @Test
    void deleteConcertServiceTest() {
        // given
        Concert testData = this.concert;

        // when
        ConcertRequestDto data = setRequestData(testData);
        concertService.deleteConcert(data);
        Concert result = concertRepository.findByConcertIdAndIsDelete(testData.getConcertId(), false);

        // then
        assertThat(result, is(nullValue()));
        assertThatThrownBy(() -> concertService.deleteConcert(data)).isInstanceOf(ConcertIdNotDataException.class);
    }

    @AfterEach()
    void deleteData() {
        concertRepository.deleteAll();
    }
}
