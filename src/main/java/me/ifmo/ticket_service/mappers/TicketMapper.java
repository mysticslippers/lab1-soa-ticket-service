package me.ifmo.ticket_service.mappers;

import me.ifmo.ticket_service.domain.Ticket;
import me.ifmo.ticket_service.web.request.TicketCreateRequest;
import me.ifmo.ticket_service.web.request.TicketUpdateRequest;
import me.ifmo.ticket_service.web.response.TicketResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CoordinatesMapper.class, EventMapper.class})
public interface TicketMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "event", ignore = true)
    Ticket toEntity(TicketCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "event", ignore = true)
    void updateEntity(TicketUpdateRequest request, @MappingTarget Ticket ticket);

    TicketResponse toResponse(Ticket ticket);
}
