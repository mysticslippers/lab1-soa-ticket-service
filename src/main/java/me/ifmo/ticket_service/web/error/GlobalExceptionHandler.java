package me.ifmo.ticket_service.web.error;

import lombok.extern.slf4j.Slf4j;
import me.ifmo.ticket_service.web.error.exceptions.BusinessRuleViolationException;
import me.ifmo.ticket_service.web.error.exceptions.ResourceConflictException;
import me.ifmo.ticket_service.web.error.exceptions.ResourceNotFoundException;
import me.ifmo.ticket_service.web.response.ApiErrorResponse;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final HttpStatusCode UNPROCESSABLE_CONTENT = HttpStatusCode.valueOf(422);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException exception) {
        return respond(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", exception.getMessage());
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleConflict(ResourceConflictException exception) {
        return respond(HttpStatus.CONFLICT, "RESOURCE_CONFLICT", exception.getMessage());
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessRule(BusinessRuleViolationException exception) {
        return respond(UNPROCESSABLE_CONTENT, "BUSINESS_RULE_VIOLATION", exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception exception) {
        log.error("Unexpected request failure", exception);
        return respond(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Внутренняя ошибка сервера");
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        if (exception instanceof MethodArgumentNotValidException validationException) {
            boolean requestBody = validationException.getParameter().hasParameterAnnotation(RequestBody.class);

            HttpStatusCode validationStatus = requestBody ? UNPROCESSABLE_CONTENT : HttpStatus.BAD_REQUEST;
            String code = requestBody ? "VALIDATION_ERROR" : "INVALID_PARAMETER";
            String message = requestBody ? "Некорректные поля запроса" : "Некорректные параметры запроса";

            return super.handleExceptionInternal(exception, error(validationStatus, code, message, validationDetails(validationException)),
                    headers, validationStatus, request);
        }

        String code = errorCode(exception, status);
        String message = errorMessage(exception, status);
        return super.handleExceptionInternal(exception, error(status, code, message, Map.of()), headers, status, request);
    }

    private static Map<String, String> validationDetails(MethodArgumentNotValidException exception) {
        Map<String, String> details = new LinkedHashMap<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors())
            details.merge(fieldError.getField(), messageOrDefault(fieldError), (first, next) -> first + "; " + next);

        for (ObjectError objectError : exception.getBindingResult().getGlobalErrors())
            details.merge("_object", messageOrDefault(objectError), (first, next) -> first + "; " + next);

        return details;
    }

    private static String messageOrDefault(ObjectError error) {
        return error.getDefaultMessage() != null ? error.getDefaultMessage() : "Некорректное значение";
    }

    private static String errorCode(Exception exception, HttpStatusCode status) {
        if (exception instanceof HttpMessageNotReadableException)
            return "INVALID_REQUEST_BODY";
        if (exception instanceof TypeMismatchException || exception instanceof MissingServletRequestParameterException)
            return "INVALID_PARAMETER";

        return switch (status.value()) {
            case 400 -> "INVALID_PARAMETER";
            case 404 -> "RESOURCE_NOT_FOUND";
            case 405 -> "METHOD_NOT_ALLOWED";
            case 406 -> "NOT_ACCEPTABLE";
            case 409 -> "RESOURCE_CONFLICT";
            case 415 -> "UNSUPPORTED_MEDIA_TYPE";
            case 422 -> "VALIDATION_ERROR";
            case 503 -> "SERVICE_UNAVAILABLE";
            case 504 -> "GATEWAY_TIMEOUT";
            default -> status.is5xxServerError() ? "INTERNAL_ERROR" : "INVALID_REQUEST";
        };
    }

    private static String errorMessage(Exception exception, HttpStatusCode status) {
        if (status.is5xxServerError())
            return "Внутренняя ошибка сервера";

        if (exception instanceof ResponseStatusException responseStatusException && responseStatusException.getReason() != null && !responseStatusException.getReason().isBlank())
            return responseStatusException.getReason();

        if (exception instanceof HttpMessageNotReadableException)
            return "Некорректное или отсутствующее тело запроса";

        return switch (status.value()) {
            case 400 -> "Некорректные параметры запроса";
            case 404 -> "Ресурс не найден";
            case 405 -> "HTTP-метод не поддерживается";
            case 406 -> "Невозможно вернуть запрошенный формат ответа";
            case 409 -> "Конфликт состояния ресурса";
            case 415 -> "Неподдерживаемый формат тела запроса";
            case 422 -> "Некорректные данные запроса";
            default -> "Ошибка обработки запроса";
        };
    }

    private static ResponseEntity<ApiErrorResponse> respond(HttpStatusCode status, String code, String message) {
        return ResponseEntity.status(status).body(error(status, code, message, Map.of()));
    }

    private static ApiErrorResponse error(HttpStatusCode status, String code, String message, Map<String, String> details) {
        return new ApiErrorResponse(status.value(), code, message, details, Instant.now());
    }
}
