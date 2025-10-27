package app.ticket.ticketing.stage;

import app.ticket.ticketing.db.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class StageController {
    private final StageService stageService;

    @GetMapping("/stage/{id}")
    public List<Stage> readStages(@PathVariable final String id) {
        return stageService.readStages(id);
    }

    @PostMapping("/stage")
    @ResponseStatus(HttpStatus.CREATED)
    public StageResponseDto createStage(@RequestBody final StageRequestDto request) {
        return stageService.createStage(request);
    }

    @PutMapping("/stage")
    public StageResponseDto updateStage(@RequestBody final StageRequestDto request) {
        return stageService.updateStage(request);
    }

    @DeleteMapping("/stage")
    public void deleteStage(@RequestBody final StageRequestDto request) {
        stageService.deleteStage(request);
    }
}
