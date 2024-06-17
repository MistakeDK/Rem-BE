package com.datnguyen.rem.service;

import com.datnguyen.rem.entity.User;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface MailService {
    void sendVerificationEmail(User user) throws MessagingException, IOException, TemplateException;
    String getEmailContent(User user) throws IOException, TemplateException;
}
