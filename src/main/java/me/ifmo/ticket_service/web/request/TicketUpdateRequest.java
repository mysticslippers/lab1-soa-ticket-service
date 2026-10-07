package me.ifmo.ticket_service.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import me.ifmo.ticket_service.domain.enums.TicketType;

@Schema(description = "Full replacement of a ticket. All fields except comment are required. id and creationDate are preserved.")
public record TicketUpdateRequest(
        @NotEmpty
        @Schema(description = "Non-empty ticket name.", example = "Main hall", minLength = 1, requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @NotNull
        @Positive
        @Schema(description = "Existing coordinates id.", example = "1", minimum = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer coordinatesId,

        @NotNull
        @Positive
        @Schema(description = "Ticket price, a positive int32 value.", example = "1500", minimum = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer price,

        @Size(min = 1)
        @Schema(description = "Optional non-empty comment. Omission or null clears it in PUT.", example = "Near the stage", minLength = 1, types = {"string", "null"}, requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String comment,

        @NotNull
        @Schema(description = "Ticket category.", example = "VIP", requiredMode = Schema.RequiredMode.REQUIRED)
        TicketType type,

        @NotNull
        @Positive
        @Schema(description = "Existing event id.", example = "15", minimum = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer eventId
) {
}
