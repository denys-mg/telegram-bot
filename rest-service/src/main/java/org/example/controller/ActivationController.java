package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.example.model.AppUser;
import org.example.service.AnswerProducer;
import org.example.service.UserActivationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class ActivationController {

    private final UserActivationService userActivationService;

    private final AnswerProducer answerProducer;

    @PostMapping("/activation")
    public ResponseEntity<?> activateEmail(
            @RequestParam("id") String id,
            @RequestParam(value = "is_active", defaultValue = "true") boolean isActive
    ) {
        String answer;
        Long chatId = null;
        try {
            AppUser persistentAppUser = userActivationService.getAppUser(id);
            chatId = persistentAppUser.getTelegramChatId();
            userActivationService.updateAppUserIsActive(persistentAppUser, isActive);
            answer = isActive
                    ? "Email successfully activated!"
                    : "Email successfully deactivated!";
            try {
                answerProducer.produceAnswer(answer, chatId);
            } catch (Exception exception) {
                log.error(answer + " But RabbitMQ is not responding!", exception);
            }
            return ResponseEntity
                    .ok()
                    .body(answer);
        } catch (Exception ex) {
            log.error("Error during email activation for user ID: {%s}".formatted(id), ex);
            answer = "Email activation failed due to server error!";
            try {
                answerProducer.produceAnswer(answer, chatId);
            } catch (Exception exception) {
                log.error(answer + " And RabbitMQ is not responding!", exception);
            }
            return ResponseEntity
                    .internalServerError()
                    .body(answer);
        }
    }
}
