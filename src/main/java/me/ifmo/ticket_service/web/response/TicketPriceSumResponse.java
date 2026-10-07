package me.ifmo.ticket_service.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Aggregate ticket price.")
public record TicketPriceSumResponse(
        @Schema(description = "Sum of all ticket prices; zero for an empty collection.", example = "3000", minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
        long sum
) {
}
