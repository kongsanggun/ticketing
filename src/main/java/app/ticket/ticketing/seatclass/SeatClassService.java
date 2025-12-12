package app.ticket.ticketing.seatclass;

import app.ticket.ticketing.common.exception.custom.seatclass.SeatClassIdNotDataException;
import app.ticket.ticketing.common.exception.custom.seatclass.SeatClassNotRemainException;
import app.ticket.ticketing.concert.ConcertRequestDto;
import app.ticket.ticketing.db.SeatClass;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional()
public class SeatClassService {
    private final SeatClassRepository seatClassRepository;

    /*
     * 공연 내 모든 가격을 조회한다.
     */
    public List<SeatClass> readSeatClass(String concertId) {
        List<SeatClass> seatClass = seatClassRepository.findByConcertId(concertId);
        if (seatClass.isEmpty()) {
            throw new SeatClassIdNotDataException(concertId);
        }
        return seatClass;
    }

    /*
     * 공연 내 가격을 추가한다.
     */
    public SeatClassResponseDto createSeatClass(SeatClassRequestDto request) {
        SeatClass seatClass = new SeatClass(request);
        seatClassRepository.saveAndFlush(seatClass);
        return new SeatClassResponseDto(seatClass);
    }

    /*
     * 공연 생성으로 인하여 공연 내 가격을 추가한다.
     */
    public void createSeatClassByConcert(ConcertRequestDto request) {
        SeatClass seatClass = new SeatClass(request);
        seatClassRepository.saveAndFlush(seatClass);
    }

    /*
     * 공연 내 가격을 수정한다.
     */
    public SeatClassResponseDto updateSeatClass(SeatClassRequestDto request) {
        SeatClass seatClass = checkExist(request);
        seatClass.setName(request.getName());
        seatClass.setPrice(request.getPrice());
        seatClassRepository.saveAndFlush(seatClass);
        return new SeatClassResponseDto(seatClass);
    }

    /*
     * 공연 내 가격을 삭제한다.
     */
    public void deleteSeatClass(SeatClassRequestDto request) {
        // 1. 삭제 이후 남아있는 가격이 존재하지 않을 경우가 있는지 확인한다.
        if (seatClassRepository.findByConcertIdAndIsDelete(
                request.getConcertId(),
                false).size() <= 1) {
            throw new SeatClassNotRemainException();
        }

        // 2. 동일한 중복요청이 있는지 확인한다.
        SeatClass seatClass = checkExist(request);
        seatClass.deleteData();
        seatClassRepository.saveAndFlush(seatClass);
    }

    /*
     * 존재하는 공연 내 가격인지 확인한다. 존재 시 해당 값을 반환한다.
     */
    private SeatClass checkExist(SeatClassRequestDto request) {
        SeatClass seatClass = seatClassRepository.findBySeatClassIdAndIsDelete(
                request.getSeatClassId(),
                false
        );
        if (seatClass == null) {
            throw new SeatClassIdNotDataException(request.getSeatClassId());
        }
        return seatClass;
    }
}
