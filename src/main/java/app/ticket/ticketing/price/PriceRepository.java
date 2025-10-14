package app.ticket.ticketing.price;

import app.ticket.ticketing.db.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
    List<Price> findByConcertId(String concertId);
    Price findByPriceIdAndConcertId(Long priceId, String concertId);
    void deleteByPriceIdAndConcertId(Long priceId, String concertId);
}
