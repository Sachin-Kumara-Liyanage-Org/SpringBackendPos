package com.techbirdssolutions.springpos.utils;

public class LogMessageCreator {
    public static String createExceptionMessage(String message, Exception e) {
        return message + " " + e.getMessage();
    }
}
