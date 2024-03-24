package com.techbirdssolutions.springpos.exception;
/**
 * The roles that the User entity has.
 * It is a many-to-many relationship with the Role entity.
 * The relationship is managed through a join table named "users_roles".
 */
public class InvalidTokenException extends Exception{
    /**
     * A constructor that initializes the InvalidTokenException with a message.
     * @param message The message of the InvalidTokenException.
     */
    public InvalidTokenException(String message) {
        super(message);
    }
}
