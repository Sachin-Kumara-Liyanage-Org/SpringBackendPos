package com.techbirdssolutions.springpos.model.custom;

import lombok.Data;

import java.io.File;
import java.util.List;
import java.util.Map;

@Data
public class EmailData {
    private List<String> to;
    private List<String> cc;
    private List<String> bcc;
    private String subject;
    private String body;
    private String replyTo;
    private String replyToName;
    private boolean isHtml;
    private Map<String, File> attachments;
}
