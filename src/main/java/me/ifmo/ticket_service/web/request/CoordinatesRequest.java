package me.ifmo.ticket_service.web.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CoordinatesRequest {

    private static final String MAX_FINITE_FLOAT = "3.4028235E38";
    private static final String MIN_FINITE_FLOAT = "-3.4028235E38";

    @NotNull
    @DecimalMin(value = "-999", inclusive = false)
    @DecimalMax(MAX_FINITE_FLOAT)
    private Float x;

    @NotNull
    @DecimalMin(MIN_FINITE_FLOAT)
    @DecimalMax(MAX_FINITE_FLOAT)
    private Float y;
}
