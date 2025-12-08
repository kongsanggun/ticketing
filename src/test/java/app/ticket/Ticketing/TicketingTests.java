package app.ticket.Ticketing;

import app.ticket.ticketing.concert.ConcertRepository;
import app.ticket.ticketing.concert.ConcertRequestDto;
import app.ticket.ticketing.db.Concert;
import app.ticket.ticketing.db.SeatClass;
import app.ticket.ticketing.db.Stage;
import app.ticket.ticketing.db.User;
import app.ticket.ticketing.seatclass.SeatClassRepository;
import app.ticket.ticketing.stage.StageRepository;

import app.ticket.ticketing.user.UserCreateRequestDto;
import app.ticket.ticketing.user.UserRepository;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public abstract class TicketingTests {
    @Autowired
    private ConcertRepository concertRepository;

    private ConcertRequestDto setConcertDto(int price) {
        return new ConcertRequestDto(
                "test",
                "test",
                "test",
                new Date(),
                new Date(),
                "test",
                price,
                2
        );
    }

    private Concert setConcert() {
        Concert concert = new Concert(setConcertDto(10000));
        concert.setConcertId("test");
        concert.setCreatedAt(new Date());
        return concert;
    }

    @Autowired
    private StageRepository stageRepository;

    private Stage setStage() {
        Stage stage = new Stage(setConcertDto(10000));
        stage.setStageId("test");
        stage.setCreatedAt(new Date());
        return stage;
    }

    @Autowired
    private SeatClassRepository seatClassRepository;

    private SeatClass setSeatClass() {
        SeatClass seatClass = new SeatClass(setConcertDto(10000));
        seatClass.setSeatClassId("test");
        seatClass.setCreatedAt(new Date());
        return seatClass;
    }

    @Autowired
    private UserRepository userRepository;

    private User setUser() {
        User user = new User(new UserCreateRequestDto("test"));
        user.setUserId("test");
        user.setPoint(50000);
        user.setCreatedAt(new Date());
        return user;
    }

    @BeforeEach()
    void setBasicData() {
        concertRepository.save(setConcert());
        stageRepository.save(setStage());
        seatClassRepository.save(setSeatClass());
        userRepository.save(setUser());

        SeatClass newSeat = new SeatClass(setConcertDto(50000));
        newSeat.setSeatClassId("test2");
        newSeat.setCreatedAt(new Date());
        seatClassRepository.save(newSeat);
    }
}
