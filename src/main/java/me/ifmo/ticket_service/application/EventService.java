package me.ifmo.ticket_service.application;

import me.ifmo.ticket_service.web.request.EventCreateRequest;
import me.ifmo.ticket_service.web.request.EventUpdateRequest;
import me.ifmo.ticket_service.web.response.EventResponse;

import java.util.List;

public interface EventService {
    EventResponse create(EventCreateRequest request);

    EventResponse getById(Integer id);

    List<EventResponse> getAll();

    EventResponse update(Integer id, EventUpdateRequest request);

    void delete(Integer id);
}
