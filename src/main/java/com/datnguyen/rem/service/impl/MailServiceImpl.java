package com.datnguyen.rem.service.impl;

import com.datnguyen.rem.entity.User;

import com.datnguyen.rem.service.MailService;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Service
public class MailServiceImpl implements MailService {
    JavaMailSender mailSender;
    Configuration configuration;
    String emailAddress="gunnyfifa3@gmail.com";
    @NonFinal
    @Value("${dev.site}")
    String siteUrl;
    @Override
    public void sendVerificationEmail(User user)
            throws MessagingException, IOException, TemplateException {
        String fromAddress=emailAddress;
        String subject = "Please verify your registration";
        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message);
        helper.setSubject(subject);
        helper.setTo(user.getEmail());
        helper.setFrom(fromAddress);
        String emailContent=getEmailContent(user);
        helper.setText(emailContent,true);
        mailSender.send(message);
    }
    public String getEmailContent(User user) throws IOException, TemplateException {
        StringWriter stringWriter=new StringWriter();
        Map<String,Object> model=new HashMap<>();
        model.put("name",user.getUsername());
        model.put("verifyURL",siteUrl+"/verify?code="+user.getVerificationCode()+"&username="+user.getUsername());
        configuration.getTemplate("verification-email.ftlh").process(model,stringWriter);
        return stringWriter.getBuffer().toString();
    }
}
