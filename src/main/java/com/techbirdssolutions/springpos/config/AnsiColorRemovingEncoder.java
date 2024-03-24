
package com.techbirdssolutions.springpos.config;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.nio.charset.StandardCharsets;

/**
 * This class extends the PatternLayoutEncoder class from the Logback library.
 * It overrides the encode method to remove ANSI color codes from the logged messages.
 * ANSI color codes are used to colorize console output, but they can make the logs hard to read when stored in a file or viewed in a log management tool.
 */
public class AnsiColorRemovingEncoder extends PatternLayoutEncoder {

    // The regular expression pattern for matching ANSI color codes
    private static final String ANSI_COLOR_PATTERN = "\u001B\\[[;\\d]*m";

    /**
     * This method overrides the encode method from the PatternLayoutEncoder class.
     * It first calls the doLayout method to format the logging event into a string.
     * Then it removes any ANSI color codes from the formatted message using the replaceAll method.
     * Finally, it converts the message into bytes using the UTF-8 character set and returns the byte array.
     * If the formatted message is null, it returns an empty byte array.
     *
     * @param event the logging event to be encoded
     * @return the encoded message as a byte array
     */
    @Override
    public byte[] encode(ILoggingEvent event) {
        String formattedMessage = layout.doLayout(event);
        if (formattedMessage != null) {
            formattedMessage = formattedMessage.replaceAll(ANSI_COLOR_PATTERN, "");
            return formattedMessage.getBytes(StandardCharsets.UTF_8);
        }
        return new byte[0];
    }
}