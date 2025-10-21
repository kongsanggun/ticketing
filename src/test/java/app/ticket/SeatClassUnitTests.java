package app.ticket;

import app.ticket.ticketing.db.SeatClass;
import app.ticket.ticketing.seatclass.SeatClassRepository;
import app.ticket.ticketing.seatclass.SeatClassRequestDto;
import app.ticket.ticketing.seatclass.SeatClassResponseDto;
import app.ticket.ticketing.seatclass.SeatClassService;
import io.hypersistence.tsid.TSID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@SpringBootTest
@Slf4j
public class SeatClassUnitTests {

    /*
        SeatClassService 내 함수들을 Test한다.
     */

    @Autowired
    private SeatClassService seatClassService;

    @Autowired
    private SeatClassRepository seatClassRepository;

    SeatClassRequestDto setRequestData(String concertId) {
        SeatClassRequestDto request = new SeatClassRequestDto();

        request.setConcertId(concertId);
        request.setName("A석");
        request.setPrice(29800);

        return request;
    }

    @DisplayName("공연 내 가격 생성 테스트")
    @Test
    void createSeatClassTest() {
        // given
        SeatClassRequestDto request = setRequestData("test");

        // when
        SeatClassResponseDto result =  seatClassService.createSeatClass(request);

        // then
        assertThat(result.getSeatClassId().length(), is(13));
        assertThat(result.getConcertId(), is("test"));
    }

    @DisplayName("공연 내 가격 조회 테스트")
    @Test
    void readSeatClassTest() {
        // given
        SeatClassRequestDto request = setRequestData("test");
        seatClassService.createSeatClass(request);

        // when
        List<SeatClass> result =  seatClassService.readSeatClass("test");

        // then
        assertThat(result.size(), not(0));
        assertThat(result.get(0).getSeatClassId().length(), is(13));
    }

    @DisplayName("공연 내 가격 수정 테스트")
    @Test
    void updateSeatClassTest() {
        // given
        List<SeatClass> readSeatClass =  seatClassService.readSeatClass("test");
        String seatClassId = readSeatClass.get(0).getSeatClassId();

        // when
        SeatClassRequestDto request = new SeatClassRequestDto();
        request.setSeatClassId(seatClassId);
        request.setName("testUpdated");
        request.setPrice(10000);

        SeatClassResponseDto result =  seatClassService.updateSeatClass(request);

        // then
        assertThat(result.getSeatClassId(), is(seatClassId));
        assertThat(result.getPrice(), is(10000));
        assertThat(result.getName(), is("testUpdated"));
    }

    @DisplayName("공연 내 가격 삭제 테스트")
    @Test
    void deleteSeatClassTest() {
        // given
        SeatClassRequestDto dataDto = setRequestData("test");
        seatClassService.createSeatClass(dataDto);
        seatClassService.createSeatClass(dataDto);

        List<SeatClass> beforeDelete =  seatClassService.readSeatClass("test");
        String seatClassId = beforeDelete.get(0).getSeatClassId();

        // when
        SeatClassRequestDto request = new SeatClassRequestDto();
        request.setConcertId("test");
        request.setSeatClassId(seatClassId);

        seatClassService.deleteSeatClass(request);
        List<SeatClass> afterDelete =  seatClassService.readSeatClass("test");

        // then
        assertThat(beforeDelete.size(), not(afterDelete.size()));
    }

    @DisplayName("존재하지 않은 공연 내 가격 테스트")
    @Test
    void notSeatClassDataTest() {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            // findByConcertId 같은 경우 값이 없을 경우 size가 0으로 반환
            seatClassRepository.findByConcertId("wrongId").get(0);
        });
        Assertions.assertThrows(NullPointerException.class, () -> {
            seatClassRepository.findBySeatClassId("wrongId").getSeatClassId();
        });
    }
}
