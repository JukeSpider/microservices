package com.juke.auth.core.config.openapi

import io.swagger.v3.oas.annotations.ExternalDocumentation
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "Auth Service API",
        version = "1.0.0",
        contact = Contact(email = "jukespider@gmail.com")
    ),
    security = [SecurityRequirement(name = "Bearer Authentication")],
    externalDocs = ExternalDocumentation(
        description = "Репозиторий проекта в Github",
        url = "https://github.com/JukeSpider/microservices"
    )
)
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
class OpenApiConfig