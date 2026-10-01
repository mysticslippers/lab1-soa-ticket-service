package me.ifmo.ticket_service.application;

import me.ifmo.ticket_service.web.request.TicketCreateRequest;
import me.ifmo.ticket_service.web.request.TicketFilterRequest;
import me.ifmo.ticket_service.web.request.TicketUpdateRequest;
import me.ifmo.ticket_service.web.response.TicketResponse;
import me.ifmo.ticket_service.web.response.TicketsByEventDeleteResponse;
import org.springframework.data.domain.Page;
import tools.jackson.databind.JsonNode;

public interface TicketService {
    TicketResponse create(TicketCreateRequest request);

    TicketResponse getById(Long id);

    Page<TicketResponse> getAll(TicketFilterRequest filter, int page, int size, String sort);

    TicketResponse update(Long id, TicketUpdateRequest request);

    TicketResponse patch(Long id, JsonNode changes);

    void delete(Long id);

    long sumPrice();

    long countByEvent(Integer eventId);

    TicketsByEventDeleteResponse deleteByEvent(Integer eventId);
}
