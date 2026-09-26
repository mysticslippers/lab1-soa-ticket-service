package me.ifmo.ticket_service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.Objects;

@Entity
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coordinates")
public class Coordinates {

    private static final String MAX_FINITE_FLOAT = "3.4028235E38";
    private static final String MIN_FINITE_FLOAT = "-3.4028235E38";

    @Id
    @Positive
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @DecimalMin(value = "-999", inclusive = false)
    @DecimalMax(MAX_FINITE_FLOAT)
    @Column(name = "x", nullable = false)
    private Float x;

    @NotNull
    @DecimalMin(MIN_FINITE_FLOAT)
    @DecimalMax(MAX_FINITE_FLOAT)
    @Column(name = "y", nullable = false)
    private Float y;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Coordinates coordinates = (Coordinates) object;
        return getId() != null && Objects.equals(getId(), coordinates.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
