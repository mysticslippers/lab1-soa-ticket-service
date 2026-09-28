package me.ifmo.ticket_service.web.response;

import me.ifmo.ticket_service.domain.enums.EventType;

import java.time.ZonedDateTime;

public record EventResponse(
        Integer id,
        String name,
        ZonedDateTime date,
        EventType type
) {
}
