package me.ifmo.ticket_service.application.impl;

import lombok.RequiredArgsConstructor;
import me.ifmo.ticket_service.persistence.CoordinatesRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoordinatesServiceImpl {

    private final CoordinatesRepository repository;
}
