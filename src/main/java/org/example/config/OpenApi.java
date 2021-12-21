package org.example.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "MyApi"))
@SecurityScheme(
        name = "bearerAuth",
        description = "Enter token without quotes",
        in = SecuritySchemeIn.HEADER,
        type = SecuritySchemeType.APIKEY,
        bearerFormat = "JWT",
        scheme = "bearer",
        paramName = "Authorization"
)
public class OpenApi {
}
