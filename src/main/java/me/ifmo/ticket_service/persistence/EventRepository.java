package me.ifmo.ticket_service.persistence;

import me.ifmo.ticket_service.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {

}
