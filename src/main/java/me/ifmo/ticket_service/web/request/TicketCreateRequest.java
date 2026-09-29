package me.ifmo.ticket_service.web.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import me.ifmo.ticket_service.domain.enums.TicketType;

public record TicketCreateRequest(
        @NotEmpty
        String name,

        @NotNull
        @Valid
        CoordinatesCreateRequest coordinates,

        @NotNull
        @Positive
        Integer price,

        @Size(min = 1)
        String comment,

        @NotNull
        TicketType type,

        @NotNull
        @Positive
        Integer eventId
) {
}
