package com.techbirdssolutions.springpos.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
/**
 * This class is used to configure the security scheme for Swagger.
 * It sets the name of the security scheme to "Authorization", the type to HTTP, the bearer format to "JWT", and the scheme to "bearer".
 * This configuration allows Swagger to generate an "Authorize" button in the UI, which can be used to enter a JWT token for testing secured endpoints.
 */
@SecurityScheme(
        name = "Authorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfiguration {

}
