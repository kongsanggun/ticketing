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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public abstract class TicketingTest {
    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private SeatClassRepository seatClassRepository;

    @Autowired
    private UserRepository userRepository;

    public Concert concert;
    public List<Stage> stageList = new ArrayList<>();
    public List<SeatClass> seatClassList = new ArrayList<>();
    public List<User> userList = new ArrayList<>();

    private ConcertRequestDto setConcertDto(int price, int capacity) {
        return new ConcertRequestDto(
                "test",
                "test",
                "test",
                new Date(),
                new Date(),
                "test",
                price,
                capacity
        );
    }

    private User setUser() {
        User user = new User(new UserCreateRequestDto("test"));
        user.chargePoint(50000);
        return user;
    }

    public void setConcert() {
        concert = new Concert(setConcertDto(0, 0));
        concertRepository.save(concert);
    }

    public void setStage(int size) {
        for(int i = 0; i < size; i++) {
            stageList.add(new Stage(concert.getConcertId(), setConcertDto( 0, 0)));
        }
        stageRepository.saveAll(stageList);
    }

    public void setSeatClass(int size, int[] priceList, int[] capacityList) {
        for(int i = 0; i < size; i++) {
            seatClassList.add(new SeatClass(
                    concert.getConcertId(),
                    setConcertDto(
                            priceList[i],
                            capacityList[i]
                    )
            ));
        }
        seatClassRepository.saveAll(seatClassList);
    }

    public void setUserData(int size) {
        for(int i = 0; i < size; i++) {
            userList.add(setUser());
        }
        userRepository.saveAll(userList);
    }

    @AfterEach()
    void deleteBasicData() {
        concertRepository.deleteAll();
        stageRepository.deleteAll();
        seatClassRepository.deleteAll();
        userRepository.deleteAll();
    }
}
