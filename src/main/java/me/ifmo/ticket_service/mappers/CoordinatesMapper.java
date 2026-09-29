package me.ifmo.ticket_service.mappers;

import me.ifmo.ticket_service.domain.Coordinates;
import me.ifmo.ticket_service.web.request.CoordinatesCreateRequest;
import me.ifmo.ticket_service.web.request.CoordinatesUpdateRequest;
import me.ifmo.ticket_service.web.response.CoordinatesResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CoordinatesMapper {
    @Mapping(target = "id", ignore = true)
    Coordinates toEntity(CoordinatesCreateRequest request);

    @Mapping(target = "id", ignore = true)
    void updateEntity(CoordinatesUpdateRequest request, @MappingTarget Coordinates coordinates);

    CoordinatesResponse toResponse(Coordinates coordinates);
}
