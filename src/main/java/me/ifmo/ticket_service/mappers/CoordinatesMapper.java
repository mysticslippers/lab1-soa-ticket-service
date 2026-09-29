package me.ifmo.ticket_service.mappers;

import me.ifmo.ticket_service.domain.Coordinates;
import me.ifmo.ticket_service.web.request.CoordinatesRequest;
import me.ifmo.ticket_service.web.response.CoordinatesResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CoordinatesMapper {
    @Mapping(target = "id", ignore = true)
    Coordinates toEntity(CoordinatesRequest coordinatesRequest);

    CoordinatesResponse toResponse(Coordinates coordinates);
}
