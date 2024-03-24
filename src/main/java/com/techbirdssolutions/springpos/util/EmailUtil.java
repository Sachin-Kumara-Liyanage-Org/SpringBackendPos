package com.techbirdssolutions.springpos.util;

import com.techbirdssolutions.springpos.model.custom.EmailData;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Component
public class EmailUtil{
    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.mail.personal}")
    private String personal;


    public void sendEmail(EmailData emailData) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(from,personal);
        helper.setTo(emailData.getTo().toArray(new String[emailData.getTo().size()]));
        if(emailData.getCc()!=null && !emailData.getCc().isEmpty()){
            helper.setCc(emailData.getCc().toArray(new String[emailData.getCc().size()]));
        }
        if(emailData.getBcc()!=null && !emailData.getBcc().isEmpty()){
            helper.setBcc(emailData.getBcc().toArray(new String[emailData.getBcc().size()]));
        }
        helper.setSubject(emailData.getSubject());
        helper.setText(emailData.getBody(),emailData.isHtml());
        helper.setReplyTo(isEmpty(emailData.getReplyTo(),from),isEmpty(emailData.getReplyToName(),personal));
        if(emailData.getAttachments()!=null && !emailData.getAttachments().isEmpty()){
            for (Entry entry : emailData.getAttachments().entrySet()) {
                helper.addAttachment(entry.getKey().toString(), (File)entry.getValue());
            }
        }
        emailSender.send(message);
    }


    private String isEmpty(String str, String defaultValue){
        return str==null || str.isEmpty()?defaultValue:str;
    }
}
