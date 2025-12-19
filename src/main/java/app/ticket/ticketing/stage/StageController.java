package app.ticket.ticketing.stage;

import app.ticket.ticketing.common.exception.ApiErrorCode;
import app.ticket.ticketing.common.exception.ExceptionCode;
import app.ticket.ticketing.db.Stage;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Stage API", description = "공연 내 시간표를 관리해주는 API입니다.")
@RequiredArgsConstructor
@RestController
public class StageController {
    private final StageService stageService;

    @ApiErrorCode(value = {ExceptionCode.NOT_DATA})
    @GetMapping("/stage/{id}")
    public List<Stage> readStages(@PathVariable final String id) {
        return stageService.readStages(id);
    }

    @ApiErrorCode(value = {ExceptionCode.NOT_DATA})
    @PostMapping("/stage")
    @ResponseStatus(HttpStatus.CREATED)
    public StageResponseDto createStage(@Valid @RequestBody final StageRequestDto request) {
        return stageService.createStage(request);
    }

    @ApiErrorCode(value = {ExceptionCode.NOT_DATA})
    @PutMapping("/stage")
    public StageResponseDto updateStage(@RequestBody final StageRequestDto request) {
        return stageService.updateStage(request);
    }

    @ApiErrorCode(value = {ExceptionCode.NOT_DATA, ExceptionCode.EMPTY_STAGE})
    @DeleteMapping("/stage")
    public void deleteStage(@RequestBody final StageRequestDto request) {
        stageService.deleteStage(request);
    }
}
