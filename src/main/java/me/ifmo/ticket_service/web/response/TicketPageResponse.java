package me.ifmo.ticket_service.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Paginated ticket result.")
public record TicketPageResponse(
        @Schema(description = "Tickets in this page.", requiredMode = Schema.RequiredMode.REQUIRED)
        List<TicketResponse> content,
        @Schema(description = "Zero-based page number.", example = "0", minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
        int page,
        @Schema(description = "Requested page size.", example = "20", minimum = "1", maximum = "100", requiredMode = Schema.RequiredMode.REQUIRED)
        int size,
        @Schema(description = "Total number of matching tickets across all pages.", example = "1", minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
        long elements,
        @Schema(description = "Total number of matching pages.", example = "1", minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
        int pages
) {
}
