package me.ifmo.ticket_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class OpenApiConfig {

    @Bean
    public OpenAPI ticketServiceOpenApi(@Value("${spring.mvc.servlet.path}") String servletPath) {
        return new OpenAPI()
                .info(new Info()
                        .title("Ticket Service API")
                        .version("1.0.0")
                        .description("REST API для управления билетами."))
                .addServersItem(new Server()
                        .url(servletPath));
    }
}
