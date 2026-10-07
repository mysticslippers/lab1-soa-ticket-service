package me.ifmo.ticket_service.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

import me.ifmo.ticket_service.domain.enums.TicketType;

import java.time.LocalDateTime;

@Schema(description = "Stored ticket, including its coordinates and event.")
public record TicketResponse(
        @Schema(description = "Server-generated ticket id.", example = "105", minimum = "1", accessMode = Schema.AccessMode.READ_ONLY, requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "Ticket name.", example = "Main hall", minLength = 1, requiredMode = Schema.RequiredMode.REQUIRED)
        String name,
        @Schema(description = "Coordinates referenced by the ticket.", requiredMode = Schema.RequiredMode.REQUIRED)
        CoordinatesResponse coordinates,
        @Schema(description = "Server-generated UTC date-time without an offset.", example = "2026-10-07T12:00:00", type = "string", format = "local-date-time", accessMode = Schema.AccessMode.READ_ONLY, requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime creationDate,
        @Schema(description = "Ticket price.", example = "1500", minimum = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        int price,
        @Schema(description = "Optional non-empty comment. Omission or null clears it in PUT.", example = "Near the stage", minLength = 1, types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        String comment,
        @Schema(description = "Ticket category.", example = "VIP", requiredMode = Schema.RequiredMode.REQUIRED)
        TicketType type,
        @Schema(description = "Event referenced by the ticket.", requiredMode = Schema.RequiredMode.REQUIRED)
        EventResponse event
) {
}
