package me.ifmo.ticket_service.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Coordinates to create.")
public record CoordinatesCreateRequest(
        @NotNull
        @DecimalMin(value = "-999", inclusive = false)
        @DecimalMax("3.4028235E38")
        @Schema(description = "Finite coordinate strictly greater than -999.", example = "12.5", minimum = "-999", exclusiveMinimum = true, maximum = "3.4028235E38", requiredMode = Schema.RequiredMode.REQUIRED)
        Float x,

        @NotNull
        @DecimalMin("-3.4028235E38")
        @DecimalMax("3.4028235E38")
        @Schema(description = "Finite coordinate.", example = "-7.25", minimum = "-3.4028235E38", maximum = "3.4028235E38", requiredMode = Schema.RequiredMode.REQUIRED)
        Float y
) {
}
