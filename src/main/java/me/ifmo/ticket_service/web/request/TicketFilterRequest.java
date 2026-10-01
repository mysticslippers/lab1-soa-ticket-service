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
        Long id,

        String name,

        @Positive
        Integer coordinatesId,

        @DecimalMax("3.4028235E38")
        @DecimalMin(value = "-999", inclusive = false)
        Float x,

        @DecimalMax("3.4028235E38")
        @DecimalMin("-3.4028235E38")
        Float y,

        @Schema(description = "UTC date and time, for example 2026-10-01T12:00:00")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime creationDate,

        @Positive
        Integer price,

        String comment,
        Boolean commentIsNull,
        TicketType type,

        @Positive
        Integer eventId,

        String eventName,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        ZonedDateTime eventDate,

        Boolean eventDateIsNull,
        EventType eventType
) {
}
