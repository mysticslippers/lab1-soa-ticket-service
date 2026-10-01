package me.ifmo.ticket_service.persistence;

import me.ifmo.ticket_service.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {

    boolean existsByEvent_Id(Integer eventId);

    boolean existsByCoordinates_Id(Integer coordinatesId);
}
