package me.ifmo.ticket_service.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import me.ifmo.ticket_service.application.EventService;
import me.ifmo.ticket_service.web.request.EventCreateRequest;
import me.ifmo.ticket_service.web.request.EventUpdateRequest;
import me.ifmo.ticket_service.web.response.EventResponse;
import me.ifmo.ticket_service.web.response.ApiErrorResponse;
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

    @Operation(summary = "Create an event", description = "Creates an event with a generated id. date is optional and accepts an ISO-8601 value with an offset. The event type field is named type.", responses = {
            @ApiResponse(responseCode = "400", description = "Malformed or missing JSON body",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "415", description = "Unsupported request content type",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Request fields violate validation constraints",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "201", description = "Event created", useReturnTypeSchema = true,
            headers = @Header(name = "Location", description = "URI of the created event", schema = @Schema(type = "string", format = "uri")))
    public ResponseEntity<EventResponse> create(@Valid @RequestBody EventCreateRequest request) {
        EventResponse response = service.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "Event found", useReturnTypeSchema = true)
    @Operation(summary = "Get an event by id", description = "Returns the stored event with the requested id.", responses = {
            @ApiResponse(responseCode = "400", description = "Invalid event id; must be a positive int32 value",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Event not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public EventResponse getById(@Parameter(description = "Event id", example = "15", schema = @Schema(minimum = "1")) @PathVariable @Positive Integer id) {
        return service.getById(id);
    }

    @GetMapping
    @ApiResponse(responseCode = "200", description = "Event collection", useReturnTypeSchema = true)
    @Operation(summary = "Get all events", description = "Returns a JSON array; an empty collection returns [].")
    public List<EventResponse> getAll() {
        return service.getAll();
    }

    @ApiResponse(responseCode = "200", description = "Event updated", useReturnTypeSchema = true)
    @Operation(summary = "Update an event", description = "Fully replaces name, date and type. name and type are required; omitting date or sending null clears it. The id is preserved.", responses = {
            @ApiResponse(responseCode = "400", description = "Invalid event id; must be a positive int32 value or malformed JSON body",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Event not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "415", description = "Unsupported request content type",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Request fields violate validation constraints",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public EventResponse update(@Parameter(description = "Event id", example = "15", schema = @Schema(minimum = "1")) @PathVariable @Positive Integer id, @Valid @RequestBody EventUpdateRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponse(responseCode = "204", description = "Event deleted", content = @Content)
    @Operation(summary = "Delete an event", description = "Deletion is rejected while tickets reference this event.", responses = {
            @ApiResponse(responseCode = "400", description = "Invalid event id; must be a positive int32 value",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Event not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Tickets reference this event",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public void delete(@Parameter(description = "Event id", example = "15", schema = @Schema(minimum = "1")) @PathVariable @Positive Integer id) {
        service.delete(id);
    }
}
