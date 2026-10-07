package me.ifmo.ticket_service.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import me.ifmo.ticket_service.domain.enums.TicketType;

@Schema(name = "TicketPatchRequest", description = "Partial ticket update. Omitted fields are preserved. Only comment accepts null.", additionalProperties = Schema.AdditionalPropertiesValue.FALSE)
public record TicketPatchSchema(
        @Schema(description = "New non-empty name.", minLength = 1, example = "Main hall", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String name,

        @Schema(description = "Existing coordinates id.", minimum = "1", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Integer coordinatesId,

        @Schema(description = "New positive price.", minimum = "1", example = "1500", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Integer price,

        @Schema(description = "Non-empty comment, or null to clear it.", minLength = 1, types = {"string", "null"}, example = "Near the stage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String comment,

        @Schema(description = "New ticket category.", example = "VIP", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        TicketType type,

        @Schema(description = "Existing event id.", minimum = "1", example = "15", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Integer eventId
) {
}
