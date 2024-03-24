package com.techbirdssolutions.springpos.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * This class represents the UserDisabledException in the application.
 * It extends the Exception class to inherit fields and behavior for exception handling.
 * The class contains a constructor that accepts a message parameter.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequestModel {
    /**
     * The username of the user attempting to authenticate.
     */
    private String username;
    /**
     * The password of the user attempting to authenticate.
     */
    private String password;
    /**
     * The refresh token of the user attempting to authenticate.
     */
    private String refreshToken;
}
