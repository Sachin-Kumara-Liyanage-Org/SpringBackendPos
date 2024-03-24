package com.techbirdssolutions.springpos.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * This class represents the ResponseModel in the application.
 * It is used to capture response data such as message, data, success status, HTTP status, and request ID.
 * Lombok annotations are used to reduce boilerplate code for getters, setters, constructors, and builder methods.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseModel {
    /**
     * The message of the response.
     */
    private String message;
    /**
     * The data of the response.
     */
    private Object data;
    /**
     * The success status of the response.
     */
    private boolean success;
    /**
     * The HTTP status of the response.
     */
    private int status;
    /**
     * The request ID of the response.
     */
    private String requestId;
}
