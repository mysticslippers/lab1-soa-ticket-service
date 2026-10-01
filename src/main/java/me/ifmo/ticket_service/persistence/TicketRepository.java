package me.ifmo.ticket_service.persistence;

import me.ifmo.ticket_service.domain.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {

    long countByEvent_Id(Integer eventId);

    boolean existsByEvent_Id(Integer eventId);

    boolean existsByCoordinates_Id(Integer coordinatesId);

    @Override
    @EntityGraph(attributePaths = {"coordinates", "event"})
    Optional<Ticket> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {"coordinates", "event"})
    Page<Ticket> findAll(Specification<Ticket> specification, Pageable pageable);

    @Query("SELECT coalesce(sum(ticket.price), 0) FROM Ticket ticket")
    long sumPrice();

    @Query("SELECT ticket.id FROM Ticket ticket " +
            "WHERE ticket.event.id = :eventId ORDER BY ticket.id")
    List<Long> findIdsByEventId(@Param("eventId") Integer eventId);
}
