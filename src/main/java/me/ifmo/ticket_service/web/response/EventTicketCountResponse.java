package me.ifmo.ticket_service.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ticket count of an existing event.")
public record EventTicketCountResponse(
        @Schema(description = "Event id.", example = "15", minimum = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer eventId,
        @Schema(description = "Number of tickets of this event.", example = "2", minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
        long count
) {
}
