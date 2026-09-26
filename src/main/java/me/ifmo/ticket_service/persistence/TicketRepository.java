package me.ifmo.ticket_service.persistence;

import me.ifmo.ticket_service.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
