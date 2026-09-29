package me.ifmo.ticket_service.web.response;

import java.util.Date;
import java.util.Map;

public record ApiErrorResponse(
        int status,
        String error,
        String message,
        Map<String, String> details,
        Date timestamp
) {
}
