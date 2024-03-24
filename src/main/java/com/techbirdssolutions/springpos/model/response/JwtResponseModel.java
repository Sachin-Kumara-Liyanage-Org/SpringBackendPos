package com.techbirdssolutions.springpos.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * This class represents the JwtResponseModel in the application.
 * It is used to capture JWT response data such as access token and refresh token.
 * Lombok annotations are used to reduce boilerplate code for getters, setters, constructors, and builder methods.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponseModel{
    /**
     * The access token of the user after successful authentication.
     */
    private String accessToken;
    /**
     * The refresh token of the user after successful authentication.
     */
    private String refreshToken;

}