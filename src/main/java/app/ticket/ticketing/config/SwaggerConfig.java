package app.ticket.ticketing.config;

import app.ticket.ticketing.common.exception.ApiErrorCode;
import app.ticket.ticketing.common.exception.ErrorResponseDto;
import app.ticket.ticketing.common.exception.ExceptionCode;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .components(new Components()
                        .addSchemas(
                                "ErrorResponseDto",
                                new Schema<ErrorResponseDto>()
                        )
                )
                .info(apiInfo());
    }

    @Bean
    public OperationCustomizer apiErrorOperationCustomizer() {
        return (operation, handlerMethod) -> {
            ApiErrorCode apiErrorCode =
                    handlerMethod.getMethodAnnotation(ApiErrorCode.class);
            if (apiErrorCode == null) {
                apiErrorCode =
                        handlerMethod.getBeanType().getAnnotation(ApiErrorCode.class);
            }
            if (apiErrorCode == null) {
                return operation;
            }

            for (ExceptionCode code : apiErrorCode.value()) {

                ApiResponse apiResponse = new ApiResponse()
                        .description(code.getMessage())
                        .content(new Content().addMediaType(
                                "application/json",
                                new MediaType().schema(
                                        new Schema<>().$ref("#/components/schemas/ErrorResponseDto")
                                )
                        ));

                operation.getResponses()
                        .addApiResponse(
                                String.valueOf(code.getHttpStatus().value()),
                                apiResponse
                        );
            }

            return operation;
        };
    }

    private Info apiInfo() {
        return new Info()
                .title("ticketing API")
                .description("ticketing API를 정리해보았습니다.")
                .version("1.0.0");
    }
}
