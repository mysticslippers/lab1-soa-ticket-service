package me.ifmo.ticket_service.application.impl;

import lombok.RequiredArgsConstructor;
import me.ifmo.ticket_service.application.EventService;
import me.ifmo.ticket_service.domain.Event;
import me.ifmo.ticket_service.mappers.EventMapper;
import me.ifmo.ticket_service.persistence.EventRepository;
import me.ifmo.ticket_service.web.error.exceptions.ResourceNotFoundException;
import me.ifmo.ticket_service.web.request.EventCreateRequest;
import me.ifmo.ticket_service.web.request.EventUpdateRequest;
import me.ifmo.ticket_service.web.response.EventResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository repository;
    private final EventMapper mapper;

    @Override
    @Transactional
    public EventResponse create(EventCreateRequest request) {
        Event event = mapper.toEntity(request);

        Event saved = repository.save(event);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponse getById(Integer id) {
        Event event = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Event with id '%s' not found".formatted(id)));

        return mapper.toResponse(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional
    public EventResponse update(Integer id, EventUpdateRequest request) {
        Event event = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Event with id '%s' not found".formatted(id)));

        mapper.updateEntity(request, event);
        Event saved = repository.save(event);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Event event = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Event with id '%s' not found".formatted(id)));

        try {
            repository.delete(event);
            repository.flush();
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Нельзя удалить событие: на него ссылаются билеты",
                    exception
            );
        }
    }
}
