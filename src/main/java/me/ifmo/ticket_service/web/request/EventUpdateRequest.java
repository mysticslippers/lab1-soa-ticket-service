package me.ifmo.ticket_service.web.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import me.ifmo.ticket_service.domain.enums.EventType;

import java.time.ZonedDateTime;

public record EventUpdateRequest(
        @NotEmpty
        String name,

        ZonedDateTime date,

        @NotNull
        EventType type
) {
}
