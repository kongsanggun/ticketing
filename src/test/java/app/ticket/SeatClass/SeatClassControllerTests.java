package app.ticket.SeatClass;

import app.ticket.StartApplication;
import app.ticket.ticketing.db.SeatClass;
import app.ticket.ticketing.seatclass.SeatClassController;
import app.ticket.ticketing.seatclass.SeatClassRepository;
import app.ticket.ticketing.seatclass.SeatClassRequestDto;
import app.ticket.ticketing.seatclass.SeatClassResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Slf4j
@ContextConfiguration(classes = StartApplication.class)
public class SeatClassControllerTests {

    /*
        SeatClass 객체를 Test한다.
     */

    @Autowired
    private SeatClassRepository seatClassRepository;

    @Autowired
    private SeatClassController seatClassController;

    private List<SeatClass> seatClass;

    SeatClassRequestDto setRequestData() {
        char seatChar = (char)(Math.round((Math.random() * 14) + 65));
        SeatClassRequestDto request = new SeatClassRequestDto();
        request.setConcertId("test");
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
        this.seatClass = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            SeatClass newData = new SeatClass(setRequestData());
            newData.setCreatedAt(new Date());
            this.seatClass.add(newData);
        }
        seatClassRepository.saveAllAndFlush(this.seatClass);
    }

    @DisplayName("stage - 생성 컨트롤러 테스트")
    @Test
    void createSeatClassControllerTest() {
        // given
        SeatClass newData = new SeatClass(setRequestData());

        // when
        SeatClassResponseDto result = seatClassController.createSeatClass(setRequestData(newData));

        // then
        assertThat(result.getSeatClassId().length(), is(13));
        assertThat(result.getConcertId(), is("test"));
    }

    @DisplayName("stage - 조회 컨트롤러 테스트")
    @Test
    void readSeatClassControllerTest() {
        // when
        List<SeatClass> result =  seatClassController.readSeatClass("test");

        // then
        assertThat(result.size(), is(5));
        for(SeatClass item : result) {
            assertThat(item.getSeatClassId().length(), is(13));
        }
    }

    @DisplayName("stage - 수정 컨트롤러 테스트")
    @Test
    void updateSeatClassControllerTest() {
        // given
        List<SeatClass> testDatas =  this.seatClass;
        SeatClass testData = testDatas.get(0);

        // when
        SeatClassRequestDto data = setRequestData(testData);
        data.setName("testUpdated");
        data.setPrice(10000);
        SeatClassResponseDto result =  seatClassController.updateSeatClass(data);

        // then
        assertThat(result.getSeatClassId(), is(data.getSeatClassId()));
        assertThat(result.getPrice(), is(10000));
        assertThat(result.getName(), is("testUpdated"));
    }

    @DisplayName("stage - 삭제 컨트롤러 테스트")
    @Test
    void deleteSeatClassControllerTest() {
        // given
        List<SeatClass> testDatas =  this.seatClass;
        SeatClass testData = testDatas.get(0);

        // when
        SeatClassRequestDto data = setRequestData(testData);
        seatClassController.deleteSeatClass(data);
        SeatClass result = seatClassRepository.findBySeatClassId(testData.getSeatClassId());

        // then
        assertThat(result, is(nullValue()));
    }

    @AfterEach()
    void deleteData() {
        for(SeatClass item : seatClassRepository.findByConcertId("test")) {
            seatClassRepository.delete(item);
        }
    }
}
