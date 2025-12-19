package app.ticket.ticketing.ticketing;

import app.ticket.ticketing.common.exception.ApiErrorCode;
import app.ticket.ticketing.common.exception.ExceptionCode;
import app.ticket.ticketing.db.Ticket;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Ticketing API", description = "티켓을 예약해주는 API입니다. (선행으로 공연 및 사용자 추가가 필요합니다.)")
@RequiredArgsConstructor
@RestController
public class TicketingController {
    private final TicketingService ticketingService;

    @ApiErrorCode(value = {
            ExceptionCode.NOT_DATA,
            ExceptionCode.SEAT_SELECTED,
            ExceptionCode.NOT_POINT
    })
    @PostMapping("/ticket/{seat}")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketingResponseDto createSeatedTicket(
            @PathVariable final int seat,
            @Valid @RequestBody final TicketingRequestDto request) {
        return ticketingService.createSeatedTicket(seat, request);
    }

    @ApiErrorCode(value = {
            ExceptionCode.NOT_DATA,
            ExceptionCode.SEAT_SELECTED,
            ExceptionCode.NOT_POINT
    })
    @PostMapping("/ticket/random")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketingResponseDto createRandomTicket(@Valid @RequestBody final TicketingRequestDto request) {
        return ticketingService.createRandomTicket(request);
    }

    @ApiErrorCode(value = {ExceptionCode.NOT_DATA})
    @DeleteMapping("/ticket")
    public void cancelTicket(@RequestBody final TicketingRequestDto request) {
        ticketingService.cancelTicket(request);
    }

    @ApiErrorCode(value = {ExceptionCode.NOT_DATA})
    @GetMapping("/ticket/{id}")
    public Ticket checkTicket(@PathVariable final String id) {
        return ticketingService.checkTicket(id);
    }
}
