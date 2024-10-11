package org.example.service;

import java.util.Map;
import javax.mail.MessagingException;
import org.example.dto.MailMessageDto;

public interface MailService {
    void sendActivationMessageUsingThymeleafTemplate(MailMessageDto mailDto,
            Map<String, String> mailProps) throws MessagingException;
}
