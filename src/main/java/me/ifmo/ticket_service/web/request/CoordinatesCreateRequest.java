package me.ifmo.ticket_service.web.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record CoordinatesCreateRequest(
        @NotNull
        @DecimalMin(value = "-999", inclusive = false)
        @DecimalMax("3.4028235E38")
        Float x,

        @NotNull
        @DecimalMin("-3.4028235E38")
        @DecimalMax("3.4028235E38")
        Float y
) {
}
