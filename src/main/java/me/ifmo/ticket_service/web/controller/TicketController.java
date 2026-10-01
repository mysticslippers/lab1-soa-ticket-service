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


}
