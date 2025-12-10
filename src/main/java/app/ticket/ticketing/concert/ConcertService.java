package app.ticket.ticketing.concert;

import app.ticket.ticketing.common.exception.custom.concert.ConcertAlreadyExistException;
import app.ticket.ticketing.common.exception.custom.concert.ConcertIdNotDataException;
import app.ticket.ticketing.db.Concert;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConcertService {

    private final ConcertRepository concertRepository;

    /*
     * 공연을 조회한다.
     */
    public Concert readConcert(String concertId) {
        Concert concert = concertRepository.findByConcertId(concertId);
        if (concert == null) {
            throw new ConcertIdNotDataException(concertId);
        }
        return concert;
    }

    /*
     * 공연을 생성한다.
     */
    public ConcertResponseDto createConcert(ConcertRequestDto request) {
        // 1. 동일한 중복요청이 있는지 확인한다.
        if (concertRepository.findByConcertId(request.getConcertId()) != null) {
            throw new ConcertAlreadyExistException(request.getConcertId());
        }

        // 2. 공연을 추가한다.
        Concert concert = new Concert(request);
        concertRepository.saveAndFlush(concert);
        return new ConcertResponseDto(concert);
    }

    /*
     * 공연을 수정한다.
     */
    public ConcertResponseDto updateConcert(ConcertRequestDto request) {
        Concert concert = checkExist(request);
        concert.setName(request.getName());
        concert.setDetail(request.getDetail());
        concert.setBookStartTime(request.getBookStartTime());
        concertRepository.saveAndFlush(concert);
        return new ConcertResponseDto(concert);
    }

    /*
     * 공연을 삭제한다. (soft-delete)
     */
    public void deleteConcert(ConcertRequestDto request) {
        Concert concert = checkExist(request);
        concert.setDeleteData();
        concertRepository.saveAndFlush(concert);
    }

    /*
     * 존재하는 공연인지 확인한다. 존재 시 해당 값을 반환한다.
     */
    private Concert checkExist(ConcertRequestDto request) {
        Concert result = concertRepository.findByConcertIdAndIsDelete(request.getConcertId(), false);
        if (result == null) {
            throw new ConcertIdNotDataException(request.getConcertId());
        }
        return result;
    }
}
