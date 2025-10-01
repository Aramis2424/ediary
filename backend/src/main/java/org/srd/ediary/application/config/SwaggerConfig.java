package org.srd.ediary.application.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;

import java.util.List;

@Configuration
public class SwaggerConfig {
    private static final List<String> EXCLUDED_PATHS = List.of(
            "/api/v1/owners",
            "/api/v1/tokens"
    );

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Swagger eDiary")
                        .description("Внешнее публичное API в идеологии REST в формате OpenAPI 3.0 проекта eDiary")
                        .version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .description("Token *required* \nBearer JWT access token")
                                        .in(SecurityScheme.In.HEADER)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    @Bean
    public GlobalOpenApiCustomizer customise() {
        return openApi -> openApi.getPaths().forEach((path, pathItem) -> {
            if (EXCLUDED_PATHS.stream().anyMatch(path::equals)) {
                return;
            }
            pathItem.readOperations().forEach(operation ->
                    operation.addParametersItem(
                            new Parameter()
                                    .in("header")
                                    .required(true)
                                    .description("Bearer JWT access token")
                                    .name("Token")
                                    .schema(new StringSchema())
                    )
            );
        });
    }
}