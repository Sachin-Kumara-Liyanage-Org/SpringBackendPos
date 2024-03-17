package com.techbirdssolutions.springpos.config;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.nio.charset.StandardCharsets;

public class AnsiColorRemovingEncoder extends PatternLayoutEncoder {

    private static final String ANSI_COLOR_PATTERN = "\u001B\\[[;\\d]*m";

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
