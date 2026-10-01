package me.ifmo.ticket_service.persistence.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import me.ifmo.ticket_service.domain.Ticket;
import me.ifmo.ticket_service.web.request.TicketFilterRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TicketSpecifications {

    public TicketSpecifications() {}

    private static void equal(List<Predicate> predicates, CriteriaBuilder builder, Expression<?> field, Object value) {
        if (value != null)
            predicates.add(builder.equal(field, value));
    }

    private static void isNull(List<Predicate> predicates, CriteriaBuilder builder, Expression<?> field, Boolean value) {
        if (value != null)
            predicates.add(value ? builder.isNull(field) : builder.isNotNull(field));
    }

    public static Specification<Ticket> withFilters(TicketFilterRequest filter) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            equal(predicates, builder, root.get("id"), filter.id());
            equal(predicates, builder, root.get("name"), filter.name());

            equal(predicates, builder, root.get("coordinates").get("id"), filter.coordinatesId());
            equal(predicates, builder, root.get("coordinates").get("x"), filter.x());
            equal(predicates, builder, root.get("coordinates").get("y"), filter.y());

            equal(predicates, builder, root.get("creationDate"), filter.creationDate());
            equal(predicates, builder, root.get("price"), filter.price());
            equal(predicates, builder, root.get("comment"), filter.comment());
            isNull(predicates, builder, root.get("comment"), filter.commentIsNull());
            equal(predicates, builder, root.get("type"), filter.type());

            equal(predicates, builder, root.get("event").get("id"), filter.eventId());
            equal(predicates, builder, root.get("event").get("name"), filter.eventName());
            equal(predicates, builder, root.get("event").get("date"), filter.eventDate());
            isNull(predicates, builder, root.get("event").get("date"), filter.eventDateIsNull());
            equal(predicates, builder, root.get("event").get("type"), filter.eventType());

            return builder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
