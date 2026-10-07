package me.ifmo.ticket_service.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Stored coordinates.")
public record CoordinatesResponse(
        @Schema(description = "Server-generated coordinates id.", example = "1", minimum = "1", accessMode = Schema.AccessMode.READ_ONLY, requiredMode = Schema.RequiredMode.REQUIRED)
        Integer id,
        @Schema(description = "Finite coordinate strictly greater than -999.", example = "12.5", minimum = "-999", exclusiveMinimum = true, maximum = "3.4028235E38", requiredMode = Schema.RequiredMode.REQUIRED)
        Float x,
        @Schema(description = "Finite coordinate.", example = "-7.25", minimum = "-3.4028235E38", maximum = "3.4028235E38", requiredMode = Schema.RequiredMode.REQUIRED)
        Float y
) {
}
