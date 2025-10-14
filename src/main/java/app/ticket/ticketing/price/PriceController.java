package app.ticket.ticketing.price;

import app.ticket.ticketing.db.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PriceController {
    private final PriceService priceService;
    @GetMapping("/price/{id}")
    public List<Price> readPrices(@PathVariable final String id) {
        return priceService.readPrices(id);
    }

    @PostMapping("/price")
    @ResponseStatus(HttpStatus.CREATED)
    public PriceResponseDto createPrice(PriceRequestDto request) {
        return priceService.createPrice(request);
    }

    @PutMapping("/price")
    public PriceResponseDto updatePrice(PriceRequestDto request) {
        return priceService.updatePrice(request);
    }

    @DeleteMapping("/price")
    public void deletePrice(@RequestBody final PriceRequestDto request) {
        priceService.deletePrice(request);
    }
}
