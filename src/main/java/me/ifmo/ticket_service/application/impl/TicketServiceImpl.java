package me.ifmo.ticket_service.application.impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import me.ifmo.ticket_service.application.TicketService;
import me.ifmo.ticket_service.domain.Coordinates;
import me.ifmo.ticket_service.domain.Event;
import me.ifmo.ticket_service.domain.Ticket;
import me.ifmo.ticket_service.domain.enums.TicketType;
import me.ifmo.ticket_service.mappers.TicketMapper;
import me.ifmo.ticket_service.persistence.CoordinatesRepository;
import me.ifmo.ticket_service.persistence.EventRepository;
import me.ifmo.ticket_service.persistence.TicketRepository;
import me.ifmo.ticket_service.persistence.specification.TicketSpecifications;
import me.ifmo.ticket_service.web.error.exceptions.InvalidRequestException;
import me.ifmo.ticket_service.web.error.exceptions.RequestValidationException;
import me.ifmo.ticket_service.web.error.exceptions.ResourceNotFoundException;
import me.ifmo.ticket_service.web.request.TicketCreateRequest;
import me.ifmo.ticket_service.web.request.TicketFilterRequest;
import me.ifmo.ticket_service.web.request.TicketUpdateRequest;
import me.ifmo.ticket_service.web.response.TicketResponse;
import me.ifmo.ticket_service.web.response.TicketsByEventDeleteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

}
