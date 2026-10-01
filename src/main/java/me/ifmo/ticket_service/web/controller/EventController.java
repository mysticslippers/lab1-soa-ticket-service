package me.ifmo.ticket_service.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import me.ifmo.ticket_service.application.EventService;
import me.ifmo.ticket_service.web.request.EventCreateRequest;
import me.ifmo.ticket_service.web.request.EventUpdateRequest;
import me.ifmo.ticket_service.web.response.EventResponse;
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
@Tag(name = "Events")
@RequiredArgsConstructor
@RequestMapping(value = "/events", produces = MediaType.APPLICATION_JSON_VALUE)
public class EventController {

    private final EventService service;

    @Operation(summary = "Create an event")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "201", description = "Event created")
    public ResponseEntity<EventResponse> create(@Valid @RequestBody EventCreateRequest request) {
        EventResponse response = service.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an event by id")
    public EventResponse getById(@PathVariable @Positive Integer id) {
        return service.getById(id);
    }

    @GetMapping
    @Operation(summary = "Get all events")
    public List<EventResponse> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Update an event")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public EventResponse update(@PathVariable @Positive Integer id, @Valid @RequestBody EventUpdateRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an event")
    public void delete(@PathVariable @Positive Integer id) {
        service.delete(id);
    }
}
