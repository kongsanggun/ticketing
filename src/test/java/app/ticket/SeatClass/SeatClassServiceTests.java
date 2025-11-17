package app.ticket.SeatClass;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import app.ticket.StartApplication;
import app.ticket.ticketing.common.exception.custom.seatclass.SeatClassIdNotDataException;
import app.ticket.ticketing.common.exception.custom.seatclass.SeatClassNotRemainException;
import app.ticket.ticketing.db.SeatClass;
import app.ticket.ticketing.seatclass.SeatClassRepository;
import app.ticket.ticketing.seatclass.SeatClassRequestDto;
import app.ticket.ticketing.seatclass.SeatClassResponseDto;
import app.ticket.ticketing.seatclass.SeatClassService;
import io.hypersistence.tsid.TSID;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@Slf4j
@ContextConfiguration(classes = StartApplication.class)
public class SeatClassServiceTests {

    /*
     * SeatClassService를 Test한다.
     */

    @Autowired
    private SeatClassRepository seatClassRepository;

    @Autowired
    private SeatClassService seatClassService;

    private String concertId;
    private List<SeatClass> seatClass;

    SeatClassRequestDto setRequestData() {
        char seatChar = (char) (Math.round((Math.random() * 14) + 65));
        SeatClassRequestDto request = new SeatClassRequestDto();
        request.setConcertId(this.concertId);
        request.setName(seatChar + "석");
        request.setPrice((int) (Math.random() * 30000));
        return request;
    }

    SeatClassRequestDto setRequestData(SeatClass seatClass) {
        SeatClassRequestDto request = new SeatClassRequestDto();
        request.setSeatClassId(seatClass.getSeatClassId());
        request.setConcertId(seatClass.getConcertId());
        request.setName(seatClass.getName());
        request.setPrice(seatClass.getPrice());
        return request;
    }

    @BeforeEach()
    void setData() {
        this.concertId = TSID.fast().toString();
        this.seatClass = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SeatClass newData = new SeatClass(setRequestData());
            newData.setCreatedAt(new Date());
            this.seatClass.add(newData);
        }
        seatClassRepository.saveAllAndFlush(this.seatClass);
    }

    @DisplayName("seatClass - 조회 서비스 테스트")
    @Test
    void readSeatClassServiceTest() {
        // when
        List<SeatClass> result = seatClassService.readSeatClass(this.concertId);

        // then
        assertThat(result.size(), is(3));
        for (SeatClass item : result) {
            assertThat(item.getSeatClassId().length(), is(13));
        }

        // then
        assertThatThrownBy(() -> seatClassService.readSeatClass("wrongTest"))
                        .isInstanceOf(SeatClassIdNotDataException.class);
    }

    @DisplayName("seatClass - 수정 서비스 테스트")
    @Test
    void updateSeatClassServiceTest() {
        // given
        List<SeatClass> testDatas = this.seatClass;
        SeatClass testData = testDatas.get(0);

        // when
        SeatClassRequestDto data = setRequestData(testData);
        data.setName("testUpdated");
        data.setPrice(10000);
        SeatClassResponseDto result = seatClassService.updateSeatClass(data);

        // then
        assertThat(result.getSeatClassId(), is(data.getSeatClassId()));
        assertThat(result.getPrice(), is(10000));
        assertThat(result.getName(), is("testUpdated"));
    }

    @DisplayName("seatClass - 삭제 서비스 테스트")
    @Test
    void deleteSeatClassServiceTest() {
        // given
        List<SeatClass> testDatas = this.seatClass;
        SeatClass testData = testDatas.get(0);

        // when
        SeatClassRequestDto data = setRequestData(testData);
        seatClassService.deleteSeatClass(data);
        SeatClass result = seatClassRepository.findBySeatClassId(testData.getSeatClassId());

        // then
        assertThat(result, is(nullValue()));

        assertThatThrownBy(() -> seatClassService.deleteSeatClass(setRequestData(testDatas.get(0))))
                        .isInstanceOf(SeatClassIdNotDataException.class);

        assertThatThrownBy(() -> {
            seatClassService.deleteSeatClass(setRequestData(testDatas.get(1)));
            seatClassService.deleteSeatClass(setRequestData(testDatas.get(2)));
        }).isInstanceOf(SeatClassNotRemainException.class);
    }

    @AfterEach()
    void deleteData() {
        seatClassRepository.deleteAll();
    }
}
