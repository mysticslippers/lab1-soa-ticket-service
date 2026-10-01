package me.ifmo.ticket_service.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import me.ifmo.ticket_service.application.TicketService;
import me.ifmo.ticket_service.web.error.exceptions.InvalidRequestException;
import me.ifmo.ticket_service.web.request.TicketCreateRequest;
import me.ifmo.ticket_service.web.request.TicketFilterRequest;
import me.ifmo.ticket_service.web.request.TicketUpdateRequest;
import me.ifmo.ticket_service.web.response.ApiErrorResponse;
import me.ifmo.ticket_service.web.response.EventTicketCountResponse;
import me.ifmo.ticket_service.web.response.TicketPageResponse;
import me.ifmo.ticket_service.web.response.TicketPriceSumResponse;
import me.ifmo.ticket_service.web.response.TicketResponse;
import me.ifmo.ticket_service.web.response.TicketsByEventDeleteResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tools.jackson.databind.JsonNode;

import java.net.URI;
import java.util.Set;

@RestController
@Tag(name = "Tickets")
@RequiredArgsConstructor
@RequestMapping(value = "/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
public class TicketController {

    private static final Set<String> QUERY_PARAMETERS = Set.of(
            "id", "name", "coordinatesId", "x", "y", "creationDate", "price", "comment", "commentIsNull",
            "type", "eventId", "eventName", "eventDate", "eventDateIsNull", "eventType", "page", "size", "sort"
    );

    private final TicketService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "201", description = "Ticket created", useReturnTypeSchema = true,
            headers = @Header(name = "Location", description = "URI of the created ticket",
                    schema = @Schema(type = "string", format = "uri")))
    @Operation(summary = "Create a ticket", description = "coordinatesId and eventId must reference existing resources.", responses = {
            @ApiResponse(responseCode = "400", description = "Malformed or missing JSON body",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Coordinates or event not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "415", description = "Unsupported content type",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Request fields violate validation constraints",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<TicketResponse> create(@Valid @RequestBody TicketCreateRequest request) {
        TicketResponse response = service.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "Ticket found", useReturnTypeSchema = true)
    @Operation(summary = "Get a ticket by id", responses = {
            @ApiResponse(responseCode = "400", description = "Invalid ticket id",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ticket not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public TicketResponse getById(@PathVariable @Positive Long id) {
        return service.getById(id);
    }

    @GetMapping
    @ApiResponse(responseCode = "200", description = "Ticket page", useReturnTypeSchema = true)
    @Operation(summary = "Get a page of tickets", description = "Filters use exact equality and are combined with AND. Dates use ISO-8601; creationDate is UTC. Sort fields are applied in the specified order; id is added as a final tie-breaker unless explicitly supplied.", responses = {
            @ApiResponse(responseCode = "400", description = "Unknown, repeated or invalid filter, page, size or sort parameter",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public TicketPageResponse getAll(
            @Valid @ModelAttribute @ParameterObject TicketFilterRequest filter,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @Parameter(description = "One or more field,direction pairs separated by semicolons. Direction is asc or desc; omitted direction means asc. Fields: id, name, coordinatesId, x, y, creationDate, price, comment, type, eventId, eventName, eventDate, eventType", example = "price,desc;name,asc")
            @RequestParam(defaultValue = "id,asc") String sort,
            @Parameter(hidden = true) @RequestParam MultiValueMap<String, String> parameters
    ) {
        validate(parameters);
        Page<TicketResponse> result = service.getAll(filter, page, size, sort);
        return new TicketPageResponse(result.getContent(), result.getNumber(), result.getSize(),
                result.getTotalElements(), result.getTotalPages());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Ticket updated", useReturnTypeSchema = true)
    @Operation(summary = "Replace a ticket", description = "All required fields must be supplied. Omitting comment clears it. id and creationDate remain unchanged.", responses = {
            @ApiResponse(responseCode = "400", description = "Invalid ticket id or malformed JSON body",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ticket, coordinates or event not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "415", description = "Unsupported content type",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Request fields violate validation constraints",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public TicketResponse update(@PathVariable @Positive Long id, @Valid @RequestBody TicketUpdateRequest request) {
        return service.update(id, request);
    }

    @PatchMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, "application/merge-patch+json"})
    @ApiResponse(responseCode = "200", description = "Ticket partially updated", useReturnTypeSchema = true)
    @Operation(summary = "Partially update a ticket", description = "Supported fields: name, coordinatesId, price, comment, type, eventId. Omitted fields are preserved. Explicit null is allowed only for comment. id and creationDate cannot be changed.", responses = {
            @ApiResponse(responseCode = "400", description = "Invalid id, JSON type, ticket type, or unsupported field",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ticket, coordinates or event not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "415", description = "Unsupported content type",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Resulting ticket fields violate validation constraints",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(type = "object"),
            examples = @ExampleObject(value = "{\"price\": 1500, \"comment\": null}")))
    public TicketResponse patch(@PathVariable @Positive Long id, @RequestBody JsonNode changes) {
        return service.patch(id, changes);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponse(responseCode = "204", description = "Ticket deleted", content = @Content)
    @Operation(summary = "Delete a ticket", responses = {
            @ApiResponse(responseCode = "400", description = "Invalid ticket id",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ticket not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public void delete(@PathVariable @Positive Long id) {
        service.delete(id);
    }

    @DeleteMapping("/by-event/{eventId}")
    @ApiResponse(responseCode = "200", description = "Deleted ticket ids and count", useReturnTypeSchema = true)
    @Operation(summary = "Delete tickets of an event", description = "An existing event with no tickets returns an empty id list and a zero count.", responses = {
            @ApiResponse(responseCode = "400", description = "Invalid event id",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Event not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public TicketsByEventDeleteResponse deleteByEvent(@PathVariable @Positive Integer eventId) {
        return service.deleteByEvent(eventId);
    }

    @GetMapping("/price/sum")
    @Operation(summary = "Get the sum of all ticket prices", description = "An empty collection returns zero.")
    public TicketPriceSumResponse sumPrice() {
        return new TicketPriceSumResponse(service.sumPrice());
    }

    @GetMapping("/count-by-event/{eventId}")
    @ApiResponse(responseCode = "200", description = "Ticket count for the event", useReturnTypeSchema = true)
    @Operation(summary = "Count tickets of an event", responses = {
            @ApiResponse(responseCode = "400", description = "Invalid event id",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Event not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public EventTicketCountResponse countByEvent(@PathVariable @Positive Integer eventId) {
        return new EventTicketCountResponse(eventId, service.countByEvent(eventId));
    }

    private static void validate(MultiValueMap<String, String> parameters) {
        parameters.forEach((name, values) -> {
            if (!QUERY_PARAMETERS.contains(name))
                throw new InvalidRequestException("Unknown query parameter '%s'".formatted(name));

            if (values.size() != 1)
                throw new InvalidRequestException("Query parameter '%s' must occur once".formatted(name));

            if (values.getFirst().isBlank() && !Set.of("name", "comment", "eventName").contains(name))
                throw new InvalidRequestException("Query parameter '%s' must not be empty".formatted(name));
        });
    }
}
