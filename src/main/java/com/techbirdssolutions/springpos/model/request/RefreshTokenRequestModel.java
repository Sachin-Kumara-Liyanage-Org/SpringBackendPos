package com.techbirdssolutions.springpos.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * This class represents the RefreshTokenRequestModel in the application.
 * It is used to capture refresh token request data such as current token and refresh token.
 * Lombok annotations are used to reduce boilerplate code for getters, setters, constructors, and builder methods.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenRequestModel {
    /**
     * The current token of the user attempting to refresh their session.
     */
    private String currentToken;
    /**
     * The refresh token of the user attempting to refresh their session.
     */
    private String refreshToken;
}
