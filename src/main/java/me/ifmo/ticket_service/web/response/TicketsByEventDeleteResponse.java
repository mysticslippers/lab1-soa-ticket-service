package me.ifmo.ticket_service.web.response;

import java.util.List;

public record TicketsByEventDeleteResponse(
        Integer eventId,
        List<Long> ticketIds,
        long count
) {
}
