package me.ifmo.ticket_service.mappers;

import me.ifmo.ticket_service.domain.Event;
import me.ifmo.ticket_service.web.request.EventRequest;
import me.ifmo.ticket_service.web.response.EventResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "id", ignore = true)
    Event toEntity(EventRequest eventRequest);

    EventResponse toResponse(Event event);
}
