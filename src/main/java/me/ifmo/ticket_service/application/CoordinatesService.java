package me.ifmo.ticket_service.application;

import me.ifmo.ticket_service.web.request.CoordinatesCreateRequest;
import me.ifmo.ticket_service.web.request.CoordinatesUpdateRequest;
import me.ifmo.ticket_service.web.response.CoordinatesResponse;

import java.util.List;

public interface CoordinatesService {
    CoordinatesResponse create(CoordinatesCreateRequest request);

    CoordinatesResponse getById(Integer id);

    List<CoordinatesResponse> getAll();

    CoordinatesResponse update(Integer id, CoordinatesUpdateRequest request);

    void delete(Integer id);
}
