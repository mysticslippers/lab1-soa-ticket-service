package me.ifmo.ticket_service.web.response;

import java.util.List;

public record TicketPageResponse(
        List<TicketResponse> content,
        int page,
        int size,
        long elements,
        int pages
) {
}
