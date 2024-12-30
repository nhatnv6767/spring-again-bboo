package com.ra.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(
                new Info()
                        .title("API-services document")
                        .version("v1.0.0")
                        .description("Description for API services")
                        .license(new License().name("API License").url("http://domain.vn/license"))
        ).servers(List.of(
                new Server().url("http://192.168.1.202:8080").description("Production server"),
                new Server().url("http://localhost:8080").description("Development server")
        ));
    }
}
