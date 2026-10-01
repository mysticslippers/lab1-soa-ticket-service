package me.ifmo.ticket_service.application.impl;

import lombok.RequiredArgsConstructor;
import me.ifmo.ticket_service.application.CoordinatesService;
import me.ifmo.ticket_service.domain.Coordinates;
import me.ifmo.ticket_service.mappers.CoordinatesMapper;
import me.ifmo.ticket_service.persistence.CoordinatesRepository;
import me.ifmo.ticket_service.persistence.TicketRepository;
import me.ifmo.ticket_service.web.error.exceptions.ResourceNotFoundException;
import me.ifmo.ticket_service.web.request.CoordinatesCreateRequest;
import me.ifmo.ticket_service.web.request.CoordinatesUpdateRequest;
import me.ifmo.ticket_service.web.response.CoordinatesResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoordinatesServiceImpl implements CoordinatesService {

    private final CoordinatesRepository repository;
    private final TicketRepository ticketRepository;
    private final CoordinatesMapper mapper;

    @Override
    @Transactional
    public CoordinatesResponse create(CoordinatesCreateRequest request){
        Coordinates coordinates = mapper.toEntity(request);

        Coordinates saved = repository.save(coordinates);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CoordinatesResponse getById(Integer id){
        Coordinates coordinates = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Coordinates with id '%s' not found".formatted(id)));

        return mapper.toResponse(coordinates);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoordinatesResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional
    public CoordinatesResponse update(Integer id, CoordinatesUpdateRequest request){
        Coordinates coordinates = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Coordinates with id '%s' not found".formatted(id)));

        mapper.updateEntity(request, coordinates);
        Coordinates saved = repository.save(coordinates);
        return mapper.toResponse(saved);
    }
}
