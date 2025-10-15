package app.ticket.ticketing.seatclass;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;
import app.ticket.ticketing.concert.ConcertRequestDto;
import app.ticket.ticketing.db.SeatClass;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SeatClassService {
    private final SeatClassRepository seatClassRepository;

    /*
     *  공연 내 모든 가격을 조회한다.
     */
    public List<SeatClass> readSeatClass(String concertId) {
        List<SeatClass> seatClass =  seatClassRepository.findByConcertId(concertId);
        if (seatClass == null) {
            throw new CustomException(ExceptionCode.NOT_DATA);
        }
        return seatClass;
    }

    /*
     *  공연 내 가격을 추가한다.
     */
    public SeatClassResponseDto createSeatClass(SeatClassRequestDto request) {
        SeatClass seatClass = new SeatClass(request);
        seatClass.setCreatedAt(new Date());
        seatClassRepository.saveAndFlush(seatClass);
        return new SeatClassResponseDto(seatClass);
    }

    /*
     *  공연 생성으로 인하여 공연 내 가격을 추가한다.
     */
    public void createSeatClassByConcert(ConcertRequestDto request) {
        SeatClass seatClass = new SeatClass(request);
        seatClass.setCreatedAt(new Date());
        seatClassRepository.saveAndFlush(seatClass);
    }

    /*
     *  공연 내 가격을 수정한다.
     */
    public SeatClassResponseDto updateSeatClass(SeatClassRequestDto request) {
        SeatClass seatClass = new SeatClass(request);
        seatClass.setUpdatedAt(new Date());
        seatClassRepository.saveAndFlush(seatClass);
        return new SeatClassResponseDto(seatClass);
    }

    /*
     *  공연 내 가격을 삭제한다.
     */
    public void deleteSeatClass(SeatClassRequestDto request) {
        // 1. 삭제 이후 남아있는 가격이 존재하지 않을 경우가 있는지 확인한다.
        if (readSeatClass(request.getConcertId()).size() < 1) {
            throw new CustomException(ExceptionCode.EMPTY_PRICE);
        }

        // 2. 동일한 중복요청이 있는지 확인한다.
        SeatClass seatClass =  seatClassRepository.findBySeatClassId(request.getSeatClassId());
        if (seatClass == null) {
            throw new CustomException(ExceptionCode.NOT_DATA);
        }

        seatClassRepository.deleteBySeatClassId(request.getSeatClassId());
    }
}
