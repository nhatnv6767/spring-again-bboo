package com.ra.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(
            @Value("${open.api.title}") String title,
            @Value("${open.api.version}") String version,
            @Value("${open.api.description}") String description,
            @Value("${open.api.license}") String license,
            @Value("${open.api.server.url}") String serverUrl,
            @Value("${open.api.server.description}") String serverDescription
    ) {
        return new OpenAPI().info(
                new Info()
                        .title(title)
                        .version(version)
                        .description(description)
                        .license(new License().name(license).url("http://domain.vn/license"))
        ).servers(List.of(
                new Server().url(serverUrl).description(serverDescription),
                new Server().url("http://localhost:8080").description("Development server")
        ));
//                .components(
//                // Mỗi khi gửi request lên server, cần phải có token JWT để xác thực (kiểu như tích hợp 1 lần, những lần sau được tích hợp sẵn vào luôn)
//                new Components()
//                        .addSecuritySchemes(
//                                "bearerAuth",
//                                new SecurityScheme()
//                                        .type(SecurityScheme.Type.HTTP)
//                                        .scheme("bearer")
//                                        .bearerFormat("JWT")
//                        )
//        ).security(List.of(new SecurityRequirement().addList("bearerAuth")))
//                ;
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("api-service")
                .packagesToScan("com.ra.controller")
                .build();
    }
}
