package me.ifmo.ticket_service.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Result of deleting tickets of an existing event.")
public record TicketsByEventDeleteResponse(
        @Schema(description = "Event whose tickets were deleted.", example = "15", minimum = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer eventId,
        @Schema(description = "Ids of deleted tickets; empty when the event has no tickets.", example = "[105, 106]", requiredMode = Schema.RequiredMode.REQUIRED)
        List<Long> ticketIds,
        @Schema(description = "Number of deleted tickets; equal to ticketIds length.", example = "2", minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
        long count
) {
}
