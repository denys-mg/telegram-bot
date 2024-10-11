package org.example.service.impl;

import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.example.dto.MailMessageDto;
import org.example.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    private final SpringTemplateEngine thymeleafTemplateEngine;

    @Value("${service.rest.url}")
    private String restServerAddress;

    @Value("${service.rest.activation-endpoint}")
    private String mailActivationEndpoint;

    @Value("classpath:/static/img/logo.jpg")
    private Resource logoImageResource;

    @Override
    public void sendActivationMessageUsingThymeleafTemplate(MailMessageDto mailDto,
            Map<String, String> mailProps) throws MessagingException {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariable(
                "activationLink", restServerAddress
                        + mailActivationEndpoint.replace("{id}", mailDto.userId()));
        String htmlBody = thymeleafTemplateEngine.process(
                mailProps.get("templatePath"), thymeleafContext);
        sendHtmlMessage(mailDto.mailTo(), mailProps.get("subject"), htmlBody);
    }

    private void sendHtmlMessage(
            String to, String subject, String htmlBody
    ) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.addInline("logo.jpg", logoImageResource);
        mailSender.send(message);
    }
}
