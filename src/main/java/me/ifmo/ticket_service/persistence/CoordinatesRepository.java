package me.ifmo.ticket_service.persistence;

import me.ifmo.ticket_service.domain.Coordinates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordinatesRepository extends JpaRepository<Coordinates, Integer> {

}
