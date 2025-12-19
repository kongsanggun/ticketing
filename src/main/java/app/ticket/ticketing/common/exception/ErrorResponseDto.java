package app.ticket.ticketing.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(name = "ErrorResponseDto", description = "공통 에러 응답 DTO")
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {

    @Schema(example = "에러 메시지")
    private String message;

    @Schema(example = "에러 상세")
    private String detail;
}
