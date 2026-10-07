package me.ifmo.ticket_service.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import me.ifmo.ticket_service.domain.enums.EventType;

import java.time.ZonedDateTime;

@Schema(description = "Full replacement of an event: name and type are required; omitting date clears it.")
public record EventUpdateRequest(
        @NotEmpty
        @Schema(description = "Non-empty event name.", example = "Concert", minLength = 1, requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @Schema(description = "Optional ISO-8601 date-time with an offset. Omission or null clears it in PUT.", example = "2026-12-01T18:00:00Z", format = "date-time", types = {"string", "null"}, requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        ZonedDateTime date,

        @NotNull
        @Schema(description = "Event type.", example = "CONCERT", requiredMode = Schema.RequiredMode.REQUIRED)
        EventType type
) {
}
