package me.ifmo.ticket_service.web.error.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class RequestValidationException extends RuntimeException {
    private final Map<String, String> details;

    public RequestValidationException(Map<String, String> details) {
        super("Invalid request body fields");
        this.details = Map.copyOf(details);
    }
}
