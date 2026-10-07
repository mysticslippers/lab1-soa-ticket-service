package me.ifmo.ticket_service.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import me.ifmo.ticket_service.domain.enums.EventType;
import me.ifmo.ticket_service.domain.enums.TicketType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Schema(description = "Optional filters. All supplied filters are combined with AND.")
public record TicketFilterRequest(
        @Positive
        @Schema(description = "Exact ticket id.", example = "105", requiredMode = Schema.RequiredMode.NOT_REQUIRED, minimum = "1")
        Long id,

        @Schema(description = "Exact ticket name; combined with other filters using AND.", example = "Main hall", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String name,

        @Positive
        @Schema(description = "Exact coordinates id.", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED, minimum = "1")
        Integer coordinatesId,

        @DecimalMax("3.4028235E38")
        @DecimalMin(value = "-999", inclusive = false)
        @Schema(description = "Exact x coordinate.", example = "12.5", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Float x,

        @DecimalMax("3.4028235E38")
        @DecimalMin("-3.4028235E38")
        @Schema(description = "Exact y coordinate.", example = "-7.25", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Float y,

        @Schema(description = "Exact UTC date-time without an offset.", type = "string", format = "local-date-time", example = "2026-10-07T12:00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime creationDate,

        @Positive
        @Schema(description = "Exact ticket price.", example = "1500", requiredMode = Schema.RequiredMode.NOT_REQUIRED, minimum = "1")
        Integer price,

        @Schema(description = "Exact ticket comment.", example = "Near the stage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String comment,
        @Schema(description = "true selects null comments; false selects non-null comments.", example = "true", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Boolean commentIsNull,
        @Schema(description = "Exact ticket category.", example = "VIP", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        TicketType type,

        @Positive
        @Schema(description = "Exact event id.", example = "15", requiredMode = Schema.RequiredMode.NOT_REQUIRED, minimum = "1")
        Integer eventId,

        @Schema(description = "Exact event name.", example = "Concert", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String eventName,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @Schema(description = "Exact event date-time with an offset. Encode the plus sign in positive offsets as %2B in query parameters.", format = "date-time", example = "2026-12-01T18:00:00Z", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        ZonedDateTime eventDate,

        @Schema(description = "true selects null event dates; false selects non-null dates.", example = "false", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Boolean eventDateIsNull,
        @Schema(description = "Exact event type; maps to Event.type.", example = "CONCERT", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        EventType eventType
) {
}
