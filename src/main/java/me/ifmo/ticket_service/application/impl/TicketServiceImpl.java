package me.ifmo.ticket_service.application.impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import me.ifmo.ticket_service.application.TicketService;
import me.ifmo.ticket_service.domain.Coordinates;
import me.ifmo.ticket_service.domain.Event;
import me.ifmo.ticket_service.domain.Ticket;
import me.ifmo.ticket_service.domain.enums.TicketType;
import me.ifmo.ticket_service.mappers.TicketMapper;
import me.ifmo.ticket_service.persistence.CoordinatesRepository;
import me.ifmo.ticket_service.persistence.EventRepository;
import me.ifmo.ticket_service.persistence.TicketRepository;
import me.ifmo.ticket_service.persistence.specification.TicketSpecifications;
import me.ifmo.ticket_service.web.error.exceptions.InvalidRequestException;
import me.ifmo.ticket_service.web.error.exceptions.RequestValidationException;
import me.ifmo.ticket_service.web.error.exceptions.ResourceNotFoundException;
import me.ifmo.ticket_service.web.request.TicketCreateRequest;
import me.ifmo.ticket_service.web.request.TicketFilterRequest;
import me.ifmo.ticket_service.web.request.TicketUpdateRequest;
import me.ifmo.ticket_service.web.response.TicketResponse;
import me.ifmo.ticket_service.web.response.TicketsByEventDeleteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private static final Map<String, String> SORT_FIELDS = Map.ofEntries(
            Map.entry("id", "id"), Map.entry("name", "name"),
            Map.entry("coordinatesId", "coordinates.id"), Map.entry("x", "coordinates.x"),
            Map.entry("y", "coordinates.y"), Map.entry("creationDate", "creationDate"),
            Map.entry("price", "price"), Map.entry("comment", "comment"), Map.entry("type", "type"),
            Map.entry("eventId", "event.id"), Map.entry("eventName", "event.name"),
            Map.entry("eventDate", "event.date"), Map.entry("eventType", "event.type")
    );

    private final TicketRepository repository;
    private final CoordinatesRepository coordinatesRepository;
    private final EventRepository eventRepository;
    private final TicketMapper mapper;
    private final Validator validator;

    @Override
    @Transactional
    public TicketResponse create(TicketCreateRequest request) {
        validateRequest(request);
        Ticket ticket = mapper.toEntity(request);

        Coordinates coordinates = coordinatesRepository.findById(request.coordinatesId()).orElseThrow(
                () -> new ResourceNotFoundException("Coordinates with id '%s' not found".formatted(request.coordinatesId())));

        Event event = eventRepository.findById(request.eventId()).orElseThrow(
                () -> new ResourceNotFoundException("Event with id '%s' not found".formatted(request.eventId())));

        ticket.setCoordinates(coordinates);
        ticket.setEvent(event);

        Ticket saved = repository.save(ticket);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TicketResponse getById(Long id) {
        Ticket ticket = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Ticket with id '%s' not found".formatted(id)));

        return mapper.toResponse(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TicketResponse> getAll(TicketFilterRequest filter, int page, int size, String sort) {
        Pageable pageable = createPageable(page, size, sort);
        return repository.findAll(TicketSpecifications.withFilters(filter), pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional
    public TicketResponse update(Long id, TicketUpdateRequest request) {
        validateRequest(request);

        Ticket ticket = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Ticket with id '%s' not found".formatted(id)));

        setCoordinatesAndEvent(request, ticket);

        Ticket saved = repository.save(ticket);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public TicketResponse patch(Long id, JsonNode changes) {
        if (changes == null || !changes.isObject())
            throw new InvalidRequestException("PATCH body must be a JSON object");

        Ticket ticket = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Ticket with id '%s' not found".formatted(id)));

        String name = ticket.getName();
        Integer coordinatesId = ticket.getCoordinates().getId();
        Integer price = ticket.getPrice();
        String comment = ticket.getComment();
        TicketType type = ticket.getType();
        Integer eventId = ticket.getEvent().getId();

        for (Map.Entry<String, JsonNode> field : changes.properties()) {
            switch (field.getKey()) {
                case "name" -> name = parseString(field.getKey(), field.getValue());
                case "coordinatesId" -> coordinatesId = parseInteger(field.getKey(), field.getValue());
                case "price" -> price = parseInteger(field.getKey(), field.getValue());
                case "comment" -> comment = parseString(field.getKey(), field.getValue());
                case "type" -> type = parseType(field.getValue());
                case "eventId" -> eventId = parseInteger(field.getKey(), field.getValue());
                default -> throw new InvalidRequestException("Field '%s' cannot be updated".formatted(field.getKey()));
            }
        }

        TicketUpdateRequest request = new TicketUpdateRequest(name, coordinatesId, price, comment, type, eventId);
        validateRequest(request);
        setCoordinatesAndEvent(request, ticket);

        Ticket saved = repository.save(ticket);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Ticket existing = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Ticket with id '%s' not found".formatted(id)));

        repository.delete(existing);
    }

    @Override
    @Transactional
    public TicketsByEventDeleteResponse deleteByEvent(Integer eventId) {
        if (!eventRepository.existsById(eventId))
            throw new ResourceNotFoundException("Event with id '%s' not found".formatted(eventId));

        List<Long> ids = repository.findIdsByEventId(eventId);
        if (!ids.isEmpty()) 
            repository.deleteAllByIdInBatch(ids);
        return new TicketsByEventDeleteResponse(eventId, List.copyOf(ids), ids.size());
    }

    @Override
    @Transactional(readOnly = true)
    public long sumPrice() {
        return repository.sumPrice();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByEvent(Integer eventId) {
        if (!eventRepository.existsById(eventId))
            throw new ResourceNotFoundException("Event with id '%s' not found".formatted(eventId));

        return repository.countByEvent_Id(eventId);
    }

    private void setCoordinatesAndEvent(TicketUpdateRequest request, Ticket ticket) {
        Coordinates coordinates = coordinatesRepository.findById(request.coordinatesId()).orElseThrow(
                () -> new ResourceNotFoundException("Coordinates with id '%s' not found".formatted(request.coordinatesId())));

        Event event = eventRepository.findById(request.eventId()).orElseThrow(
                () -> new ResourceNotFoundException("Event with id '%s' not found".formatted(request.eventId())));

        mapper.updateEntity(request, ticket);
        ticket.setCoordinates(coordinates);
        ticket.setEvent(event);
    }

    private void validateRequest(Object request) {
        if (request == null)
            throw new InvalidRequestException("Request body is required");

        Map<String, String> details = new LinkedHashMap<>();
        for (ConstraintViolation<Object> violation : validator.validate(request))
            details.merge(violation.getPropertyPath().toString(), violation.getMessage(), (first, next) -> first + "; " + next);

        if (!details.isEmpty())
            throw new RequestValidationException(details);
    }

    private static String parseString(String field, JsonNode value) {
        if (value.isNull())
            return null;

        if (!value.isString())
            throw new InvalidRequestException("Field '%s' must be a string or null".formatted(field));

        return value.stringValue();
    }

    private static Integer parseInteger(String field, JsonNode value) {
        if (value.isNull())
            return null;
        
        if (!value.isIntegralNumber() || !value.canConvertToInt())
            throw new InvalidRequestException("Field '%s' must be an integer or null".formatted(field));
        
        return value.asInt();
    }

    private static TicketType parseType(JsonNode value) {
        String type = parseString("type", value);
        if (type == null)
            return null;

        try {
            return TicketType.valueOf(type);
        } catch (IllegalArgumentException exception) {
            throw new InvalidRequestException("Unknown ticket type '%s'".formatted(type));
        }
    }

    private static Pageable createPageable(int page, int size, String value) {
        if (page < 0 || size < 1 || size > 100)
            throw new InvalidRequestException("Page must be non-negative and size must be between 1 and 100");

        String[] parts = (value == null ? "id,asc" : value).split(",", -1);
        if (parts.length < 1 || parts.length > 2)
            throw new InvalidRequestException("Sort must use 'field,asc' or 'field,desc'");

        String property = SORT_FIELDS.get(parts[0].trim());
        if (property == null)
            throw new InvalidRequestException("Unknown sort field '%s'".formatted(parts[0]));

        String direction = parts.length == 2 ? parts[1].trim().toLowerCase(Locale.ROOT) : "asc";
        Sort.Direction order = switch (direction) {
            case "asc" -> Sort.Direction.ASC;
            case "desc" -> Sort.Direction.DESC;
            default -> throw new InvalidRequestException("Sort direction must be 'asc' or 'desc'");
        };

        Sort sort = Sort.by(order, property);
        if (!property.equals("id"))
            sort = sort.and(Sort.by("id"));

        return PageRequest.of(page, size, sort);
    }
}
