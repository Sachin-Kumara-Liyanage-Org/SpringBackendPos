package com.techbirdssolutions.springpos.model.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordResetRequest {
    private String email;

    private String callbackUrl;

    private String placeholder;
}
