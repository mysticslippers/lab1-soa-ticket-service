package me.ifmo.ticket_service.web.response;

public record EventTicketCountResponse(
        Integer eventId,
        long count
) {
}
