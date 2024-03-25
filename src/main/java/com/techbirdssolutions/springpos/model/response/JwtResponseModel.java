package com.techbirdssolutions.springpos.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

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
    /**
     * The email of the user after successful authentication.
     */
    private String email;
    /**
     * A map containing the privileges and roles of the user after successful authentication.
     * The key of the outer map is the name of the privilege or role.
     * The key of the inner map is the ID of the privilege or role, and the value is the name of the privilege or role.
     */
    private Map<String, Map<Long,String>> privilegesAndRoles;

}