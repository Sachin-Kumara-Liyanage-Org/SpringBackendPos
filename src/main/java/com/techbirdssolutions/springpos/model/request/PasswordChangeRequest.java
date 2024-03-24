package com.techbirdssolutions.springpos.model.request;

import lombok.Data;

@Data
public class PasswordChangeRequest {
    private String oldPassword;
    private String newPassword;
    private String token;

}
