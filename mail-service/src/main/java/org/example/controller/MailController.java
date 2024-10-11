package org.example.controller;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.example.dto.MailMessageDto;
import org.example.service.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailController {

    private static final Map<String, Map<String, String>> mailTopicProps;

    static {
        mailTopicProps = new HashMap<>();

        Map<String, String> props = new HashMap<>();
        props.put("subject", "E-mail activation message");
        props.put("templatePath", "template-activation");
        mailTopicProps.put("activation", props);
    }

    private final MailService mailService;

    @PostMapping("/send/activation")
    public ResponseEntity<String> createHtmlActivationMail(
            @RequestBody MailMessageDto mailMessageDto
    ) {
        try {
            mailService.sendActivationMessageUsingThymeleafTemplate(
                    mailMessageDto,
                    mailTopicProps.get("activation"));
            return ResponseEntity
                    .ok()
                    .body("The activation link has been sent to your email");
        } catch (Exception ex) {
            log.error("MailService unable to send "
                    + "activation message to the client", ex);
            return ResponseEntity
                    .internalServerError()
                    .body("Can't send mail activation message. Please try later!");
        }
    }
}
