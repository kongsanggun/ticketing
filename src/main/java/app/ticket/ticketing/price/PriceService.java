package app.ticket.ticketing.price;

import app.ticket.ticketing.common.exception.CustomException;
import app.ticket.ticketing.common.exception.ExceptionCode;
import app.ticket.ticketing.db.Price;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional()
public class PriceService {
    private final PriceRepository priceRepository;

    /*
     *  공연 내 모든 가격을 조회한다.
     */
    public List<Price> readPrices(String concertId) {
        List<Price> price =  priceRepository.findByConcertId(concertId);
        if (price == null) {
            throw new CustomException(ExceptionCode.NOT_DATA);
        }
        return price;
    }

    /*
     *  공연 내 가격을 추가한다.
     */
    public PriceResponseDto createPrice(PriceRequestDto request) {
        Price price = new Price(request);
        priceRepository.saveAndFlush(price);
        return new PriceResponseDto(price);
    }

    /*
     *  공연 내 가격을 수정한다.
     */
    public PriceResponseDto updatePrice(PriceRequestDto request) {
        Price price = new Price(request);
        priceRepository.saveAndFlush(price);
        return new PriceResponseDto(price);
    }

    /*
     *  공연 내 가격을 삭제한다.
     */
    public void deletePrice(PriceRequestDto request) {
        // 1. 삭제 이후 남아있는 가격이 존재하지 않을 경우가 있는지 확인한다.
        if (readPrices(request.getConcertId()).size() < 1) {
            throw new CustomException(ExceptionCode.EMPTY_PRICE);
        }

        // 2. 동일한 중복요청이 있는지 확인한다.
        Price price =  priceRepository.findByPriceIdAndConcertId(request.getPriceId(), request.getConcertId());
        if (price == null) {
            throw new CustomException(ExceptionCode.NOT_DATA);
        }

        priceRepository.deleteByPriceIdAndConcertId(request.getPriceId(), request.getConcertId());
    }
}
