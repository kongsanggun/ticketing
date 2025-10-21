package app.ticket;

import app.ticket.ticketing.concert.ConcertRepository;
import app.ticket.ticketing.concert.ConcertRequestDto;
import app.ticket.ticketing.concert.ConcertResponseDto;
import app.ticket.ticketing.concert.ConcertService;
import app.ticket.ticketing.db.Concert;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Slf4j
public class ConcertUnitTests {

    /*
        ConcertService 내 함수들을 Test한다.
     */

    @Autowired
    private ConcertService concertService;

    @Autowired
    private ConcertRepository concertRepository;

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

    @DisplayName("공연 생성 테스트")
    @Test
    void createConcertTest() {
        // given
        ConcertRequestDto request = setRequestData();

        // when
        ConcertResponseDto result =  concertService.createConcert(request);

        // then
        assertThat(result.getConcertId().length(), is(13));
        assertThat(result.getName(), is("test"));
    }

    @DisplayName("공연 조회 테스트")
    @Test
    void readConcertTest() {
        // given
        ConcertRequestDto request = setRequestData();
        ConcertResponseDto createDto = concertService.createConcert(request);

        // when
        Concert result =  concertService.readConcert(createDto.getConcertId());

        // then
        assertThat(result.getConcertId().length(), is(13));
        assertThat(result.getConcertId(), is(createDto.getConcertId()));
    }

    @DisplayName("공연 수정 테스트")
    @Test
    void updateConcertTest() {
        // given
        ConcertRequestDto request = setRequestData();
        ConcertResponseDto createDto = concertService.createConcert(request);

        // when
        request.setConcertId(createDto.getConcertId());
        request.setName("testUpdated");
        ConcertResponseDto result =  concertService.updateConcert(request);

        // then
        assertThat(result.getConcertId(), is(createDto.getConcertId()));
        assertThat(result.getName(), is("testUpdated"));
    }

    @DisplayName("공연 삭제 테스트")
    @Test
    void deleteConcertTest() {
        // given
        ConcertRequestDto request = setRequestData();
        ConcertResponseDto createDto = concertService.createConcert(request);

        // when
        request.setConcertId(createDto.getConcertId());
        concertService.deleteConcert(request);

        Concert result = concertService.readConcert(createDto.getConcertId());

        // then
        assertThat(result.getConcertId(), is(createDto.getConcertId()));
        assertThat(result.getIsDelete(), is(true));
    }

    @DisplayName("존재하지 않은 공연 테스트")
    @Test
    void notConcertDataTest() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            concertRepository.findByConcertId("wrongId").getConcertId();
        });
        Assertions.assertThrows(NullPointerException.class, () -> {
            concertRepository.findByConcertIdAndIsDelete("wrongId", true).getConcertId();
        });
    }
}
