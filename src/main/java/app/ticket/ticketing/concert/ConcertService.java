package app.ticket.ticketing.concert;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;
import app.ticket.ticketing.db.Concert;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class ConcertService {

    private final ConcertRepository concertRepository;

    /*
     *  공연을 조회한다.
     */
    public Concert readConcert(String concertId) {
        Concert concert =  concertRepository.findByConcertId(concertId);
        if (concert == null) {
            throw new CustomException(ExceptionCode.NOT_DATA);
        }
        return concert;
    }

    /*
     *  공연을 생성한다.
     */
    public ConcertResponseDto createConcert(ConcertRequestDto request) {
        // 1. 동일한 중복요청이 있는지 확인한다.
        if(concertRepository.findByConcertId(request.getConcertId()) != null) {
            throw new CustomException(ExceptionCode.ADDED_SHOW);
        }

        // 2. 공연을 추가한다.
        Concert concert = new Concert(request);
        concert.setCreatedAt(new Date());
        concertRepository.saveAndFlush(concert);
        return new ConcertResponseDto(concert);
    }

    /*
     *  공연을 수정한다.
     */
    public ConcertResponseDto updateConcert(ConcertRequestDto request) {
        Concert concert = checkExist(request);
        concert.setName(request.getName());
        concert.setDetail(request.getDetail());
        concert.setBookStartTime(request.getBookStartTime());
        concert.setUpdatedAt(new Date());
        concertRepository.saveAndFlush(concert);
        return new ConcertResponseDto(concert);
    }

    /*
     *  공연을 삭제한다. (soft-delete)
     */
    public void deleteConcert(ConcertRequestDto request) {
        Concert concert = checkExist(request);
        concert.setDeletedAt(new Date());
        concert.setIsDelete(true);
        concertRepository.saveAndFlush(concert);
    }

    /*
     *  존재하는 공연인지 확인한다.
     *  존재 시 해당 값을 반환한다.
     */
    private Concert checkExist(ConcertRequestDto request) {
        Concert result = concertRepository.findByConcertIdAndIsDelete(request.getConcertId(), false);
        if(result == null) {
            throw new CustomException(ExceptionCode.NOT_DATA);
        }
        return result;
    }
}
