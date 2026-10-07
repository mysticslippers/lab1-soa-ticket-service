package me.ifmo.ticket_service.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@Tag(name = "Documentation")
public class RedirectController {

    @GetMapping("/")
    @Operation(summary = "Redirect to Swagger UI", description = "Returns an HTTP redirect with no response body. Location includes the application's context path and servlet prefix.")
    @ApiResponse(responseCode = "302", description = "Redirect to the interactive API documentation", content = @Content,
            headers = @Header(name = "Location", description = "Swagger UI path; /api/swagger-ui/index.html with the default configuration",
                    schema = @Schema(type = "string", format = "uri-reference", example = "/api/swagger-ui/index.html")))
    public ResponseEntity<Void> redirect(HttpServletRequest request) {
        URI location = URI.create(request.getContextPath() + request.getServletPath() + "/swagger-ui/index.html");
        return ResponseEntity.status(HttpStatus.FOUND).location(location).build();
    }
}
