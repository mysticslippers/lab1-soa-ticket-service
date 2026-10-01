package me.ifmo.ticket_service.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import me.ifmo.ticket_service.application.CoordinatesService;
import me.ifmo.ticket_service.web.request.CoordinatesCreateRequest;
import me.ifmo.ticket_service.web.request.CoordinatesUpdateRequest;
import me.ifmo.ticket_service.web.response.CoordinatesResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Tag(name = "Coordinates")
@RequestMapping(value = "/coordinates", produces = MediaType.APPLICATION_JSON_VALUE)
public class CoordinatesController {

    private final CoordinatesService service;

    @Operation(summary = "Create coordinates")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "201", description = "Coordinates created")
    public ResponseEntity<CoordinatesResponse> create(@Valid @RequestBody CoordinatesCreateRequest request) {
        CoordinatesResponse response = service.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get coordinates by id")
    public CoordinatesResponse getById(@PathVariable @Positive Integer id) {
        return service.getById(id);
    }

    @GetMapping
    @Operation(summary = "Get all coordinates")
    public List<CoordinatesResponse> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Update coordinates")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CoordinatesResponse update(@PathVariable @Positive Integer id, @Valid @RequestBody CoordinatesUpdateRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete coordinates")
    public void delete(@PathVariable @Positive Integer id) {
        service.delete(id);
    }
}
