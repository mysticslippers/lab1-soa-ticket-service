package me.ifmo.ticket_service.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

import me.ifmo.ticket_service.domain.enums.EventType;

import java.time.ZonedDateTime;

@Schema(description = "Stored event. The JSON field is named type.")
public record EventResponse(
        @Schema(description = "Server-generated event id.", example = "15", minimum = "1", accessMode = Schema.AccessMode.READ_ONLY, requiredMode = Schema.RequiredMode.REQUIRED)
        Integer id,
        @Schema(description = "Non-empty event name.", example = "Concert", minLength = 1, requiredMode = Schema.RequiredMode.REQUIRED)
        String name,
        @Schema(description = "Optional ISO-8601 date-time with an offset. Omission or null clears it in PUT.", example = "2026-12-01T18:00:00Z", format = "date-time", types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        ZonedDateTime date,
        @Schema(description = "Event type.", example = "CONCERT", requiredMode = Schema.RequiredMode.REQUIRED)
        EventType type
) {
}
