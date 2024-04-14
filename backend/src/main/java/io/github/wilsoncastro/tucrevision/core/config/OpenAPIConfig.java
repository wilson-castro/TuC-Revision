package io.github.wilsoncastro.tucrevision.core.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;


@Configuration
public class OpenAPIConfig {

    @Value("${app.version}")
    private String apiVersion;

    @Value("${app.name}")
    private String apiName;

    @Value("${app.description}")
    private String apiDescription;

    @Bean
    public OpenAPI customizeOpenAPI() {
        return new OpenAPI()
                .info(
                    new Info()
                        .title(apiName)
                        .version(apiVersion)
                        .description(apiDescription)
                        .summary("API para gerenciamento")
                        .termsOfService("")
                        .contact(
                            new Contact()
                                    .name("BigData Fortaleza")
                                    .email("bigdatafortaleza@sis.fortaleza.ce.gov.br")
                                    .url("http://localhost:8080/")
                        )
                )
                .servers(Collections.singletonList(
                    new Server()
                        .url("http://localhost:8080/")
                        .description("Localhost server")
                ))
                .addSecurityItem(
                    new SecurityRequirement()
                        .addList("bearer-jwt")
                )
                .components(
                    new Components()
                        .addSecuritySchemes(
                        "bearer-jwt",
                            new SecurityScheme()
                                .name("bearer-jwt")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .description("Forneça o Token JWT. Com ele você poderá acessar os endpoints da API.")
                                .bearerFormat("JWT")
                        )
                );
    }

}
