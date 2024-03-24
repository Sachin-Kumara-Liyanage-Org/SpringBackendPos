package com.techbirdssolutions.springpos.exception;
/**
 * This class represents the UserDisabledException in the application.
 * It extends the Exception class to inherit fields and behavior for exception handling.
 * The class contains a constructor that accepts a message parameter.
 */
public class UserDisabledException extends Exception{
    /**
     * A constructor that initializes the UserDisabledException with a message.
     * @param message The message of the UserDisabledException.
     */
    public UserDisabledException(String message) {
        super(message);
    }

}
