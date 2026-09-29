package me.ifmo.ticket_service.mappers;

import me.ifmo.ticket_service.domain.Event;
import me.ifmo.ticket_service.web.request.EventCreateRequest;
import me.ifmo.ticket_service.web.request.EventUpdateRequest;
import me.ifmo.ticket_service.web.response.EventResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "id", ignore = true)
    Event toEntity(EventCreateRequest request);

    @Mapping(target = "id", ignore = true)
    void updateEntity(EventUpdateRequest request, @MappingTarget Event event);

    EventResponse toResponse(Event event);
}
