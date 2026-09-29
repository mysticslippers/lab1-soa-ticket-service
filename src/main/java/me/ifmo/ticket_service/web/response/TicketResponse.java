package me.ifmo.ticket_service.web.response;

import me.ifmo.ticket_service.domain.enums.TicketType;

import java.time.LocalDateTime;

public record TicketResponse(
        Long id,
        String name,
        CoordinatesResponse coordinates,
        LocalDateTime creationDate,
        int price,
        String comment,
        TicketType type,
        EventResponse event
) {
}
