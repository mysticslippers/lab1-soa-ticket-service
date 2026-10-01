package me.ifmo.ticket_service.web.error;

import lombok.extern.slf4j.Slf4j;
import me.ifmo.ticket_service.web.error.exceptions.BusinessRuleViolationException;
import me.ifmo.ticket_service.web.error.exceptions.InvalidRequestException;
import me.ifmo.ticket_service.web.error.exceptions.RequestValidationException;
import me.ifmo.ticket_service.web.error.exceptions.ResourceConflictException;
import me.ifmo.ticket_service.web.error.exceptions.ResourceNotFoundException;
import me.ifmo.ticket_service.web.response.ApiErrorResponse;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
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

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidRequest(InvalidRequestException exception) {
        return respond(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", exception.getMessage());
    }

    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleRequestValidation(RequestValidationException exception) {
        return ResponseEntity.status(UNPROCESSABLE_CONTENT)
                .body(error(UNPROCESSABLE_CONTENT, "VALIDATION_ERROR", exception.getMessage(), exception.getDetails()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception exception) {
        log.error("Unexpected request failure", exception);
        return respond(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Internal server error");
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        if (exception instanceof MethodArgumentNotValidException validationException) {
            boolean requestBody = validationException.getParameter().hasParameterAnnotation(RequestBody.class);

            HttpStatusCode validationStatus = requestBody ? UNPROCESSABLE_CONTENT : HttpStatus.BAD_REQUEST;
            String code = requestBody ? "VALIDATION_ERROR" : "INVALID_PARAMETER";
            String message = requestBody ? "Invalid request body fields" : "Invalid request parameters";

            return super.handleExceptionInternal(exception, error(validationStatus, code, message, validationDetails(validationException.getBindingResult())),
                    headers, validationStatus, request);
        }

        if (exception instanceof HandlerMethodValidationException validationException && !validationException.isForReturnValue()) {
            boolean requestBodyOnly = !validationException.getParameterValidationResults().isEmpty()
                    && validationException.getCrossParameterValidationResults().isEmpty()
                    && validationException.getParameterValidationResults().stream()
                    .allMatch(result -> result.getMethodParameter().hasParameterAnnotation(RequestBody.class));

            HttpStatusCode validationStatus = requestBodyOnly ? UNPROCESSABLE_CONTENT : HttpStatus.BAD_REQUEST;
            String code = requestBodyOnly ? "VALIDATION_ERROR" : "INVALID_PARAMETER";
            String message = requestBodyOnly ? "Invalid request body fields" : "Invalid request parameters";

            return super.handleExceptionInternal(exception, error(validationStatus, code, message, methodValidationDetails(validationException)),
                    headers, validationStatus, request);
        }

        String code = errorCode(exception, status);
        String message = errorMessage(exception, status);
        return super.handleExceptionInternal(exception, error(status, code, message, Map.of()), headers, status, request);
    }

    private static Map<String, String> validationDetails(Errors errors) {
        Map<String, String> details = new LinkedHashMap<>();
        for (FieldError fieldError : errors.getFieldErrors())
            details.merge(fieldError.getField(), messageOrDefault(fieldError), (first, next) -> first + "; " + next);

        for (ObjectError objectError : errors.getGlobalErrors())
            details.merge("_object", messageOrDefault(objectError), (first, next) -> first + "; " + next);

        return details;
    }

    private static Map<String, String> methodValidationDetails(HandlerMethodValidationException exception) {
        Map<String, String> details = new LinkedHashMap<>();
        for (ParameterValidationResult result : exception.getParameterValidationResults()) {
            if (result instanceof ParameterErrors errors) {
                validationDetails(errors).forEach((field, message) -> details.merge(field, message, (first, next) -> first + "; " + next));
            } else {
                String parameter = result.getMethodParameter().getParameterName();
                String key = parameter != null ? parameter : "parameter";
                for (MessageSourceResolvable error : result.getResolvableErrors())
                    details.merge(key, messageOrDefault(error), (first, next) -> first + "; " + next);
            }
        }
        for (MessageSourceResolvable error : exception.getCrossParameterValidationResults())
            details.merge("_request", messageOrDefault(error), (first, next) -> first + "; " + next);
        return details;
    }

    private static String messageOrDefault(MessageSourceResolvable error) {
        return error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value";
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
            return "Internal server error";

        if (exception instanceof ResponseStatusException responseStatusException && responseStatusException.getReason() != null && !responseStatusException.getReason().isBlank())
            return responseStatusException.getReason();

        if (exception instanceof HttpMessageNotReadableException)
            return "Malformed or missing request body";

        return switch (status.value()) {
            case 400 -> "Invalid request parameters";
            case 404 -> "Resource not found";
            case 405 -> "HTTP method not allowed";
            case 406 -> "Cannot produce the requested response format";
            case 409 -> "Resource state conflict";
            case 415 -> "Unsupported request content type";
            case 422 -> "Invalid request data";
            default -> "Request processing error";
        };
    }

    private static ResponseEntity<ApiErrorResponse> respond(HttpStatusCode status, String code, String message) {
        return ResponseEntity.status(status).body(error(status, code, message, Map.of()));
    }

    private static ApiErrorResponse error(HttpStatusCode status, String code, String message, Map<String, String> details) {
        return new ApiErrorResponse(status.value(), code, message, details, new Date());
    }
}
