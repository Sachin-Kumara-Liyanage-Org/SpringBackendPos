package com.techbirdssolutions.springpos.exception;
/**
 * This class represents the LicenseExpiredException in the application.
 * It extends the Exception class to inherit fields and behavior for exception handling.
 * The class contains a constructor that accepts a message parameter.
 */
public class LicenseExpiredException extends Exception{
    /**
     * A constructor that initializes the LicenseExpiredException with a message.
     * @param message The message of the LicenseExpiredException.
     */
    public LicenseExpiredException(String message) {
        super(message);
    }

}
