package com.techbirdssolutions.springpos.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseModel {
    private String message;
    private Object data;
    private boolean success;
    private int status;
}
