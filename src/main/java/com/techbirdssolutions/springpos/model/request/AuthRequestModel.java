package com.techbirdssolutions.springpos.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequestModel {
    private String username;
    private String password;
    private String refreshToken;
}
