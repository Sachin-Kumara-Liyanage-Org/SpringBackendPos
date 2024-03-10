package com.techbirdssolutions.springpos.exception;

public class InvalidTokenException extends Exception{
    public InvalidTokenException(String message) {
        super(message);
    }
}
