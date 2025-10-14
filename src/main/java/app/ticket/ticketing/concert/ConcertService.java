package app.ticket.ticketing.concert;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;
import app.ticket.ticketing.db.Price;
import app.ticket.ticketing.db.Concert;

import app.ticket.ticketing.db.Stage;
import app.ticket.ticketing.price.PriceRepository;
import app.ticket.ticketing.stage.StageRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional()
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final StageRepository stageRepository;
    private final PriceRepository priceRepository;

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
        Concert concert = new Concert(request);

        // 1. 동일한 중복요청이 있는지 확인한다.
        checkDuplicateRequest(concert);

        // 2. 공연을 추가한다.
        stageRepository.save(new Stage(request));
        priceRepository.save(new Price(request));
        concertRepository.saveAndFlush(concert);

        return new ConcertResponseDto(concert);
    }

    private void checkDuplicateRequest(Concert concert) {
        Concert duplicate = concertRepository.findByConcertId(concert.getConcertId());
        if(duplicate != null) {
            throw new CustomException(ExceptionCode.ADDED_SHOW);
        }
    }

    /*
     *  공연을 수정한다.
     */
    public ConcertResponseDto updateConcert(ConcertRequestDto request) {
        if (concertRepository.findByConcertId(request.getConcertId()) == null) {
            throw new CustomException(ExceptionCode.NOT_DATA);
        }

        Concert concert = new Concert(request);
        concertRepository.saveAndFlush(concert);
        return new ConcertResponseDto(concert);
    }

    /*
     *  공연을 삭제한다. (soft-delete)
     */
    public void deleteConcert(ConcertRequestDto request) {
        concertRepository.findByConcertIdIdForDelete(request.getConcertId()).orElseThrow(() -> {
            throw new CustomException(ExceptionCode.NOT_DATA);
        });
        concertRepository.deleteByConcertId(request.getConcertId(), true);
    }
}
