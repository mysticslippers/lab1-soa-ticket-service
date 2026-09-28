package me.ifmo.ticket_service.web.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import me.ifmo.ticket_service.domain.enums.EventType;

public record EventRequest(
        @NotNull
        @NotEmpty
        String name,

        @NotNull
        EventType type
) {
}
