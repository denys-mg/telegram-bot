package org.example.service.impl;

import static org.example.enums.BotCommand.CANCEL;
import static org.example.enums.BotCommand.EMAIL_CHANGE;
import static org.example.model.enums.UserState.BASIC_STATE;
import static org.example.model.enums.UserState.WAIT_FOR_EMAIL_STATE;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.example.client.MailServiceClient;
import org.example.model.AppUser;
import org.example.repository.AppUserRepository;
import org.example.service.AnswerProducer;
import org.example.service.AppUserService;
import org.example.util.EmailValidator;
import org.springframework.stereotype.Service;

@Log4j
@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AnswerProducer answerProducer;

    private final AppUserRepository appUserRepository;

    private final MailServiceClient mailServiceClient;

    @Override
    public String handleRegistration(AppUser appUser) {
        if (appUser.getIsActive()) {
            return "You already registered!"
                    + "\nIf you want to change your account email - write " + EMAIL_CHANGE;
        }
        return handleEmailOperation(appUser);
    }

    @Override
    public String handleEmailChange(AppUser appUser) {
        return handleEmailOperation(appUser);
    }

    @Override
    public String setEmail(String email, AppUser appUser) {
        if (!EmailValidator.isValidEmail(email)) {
            return "Please write correct email"
                    + "\nOtherwise cancel registration by " + CANCEL;
        }
        if (!isAvailableEmail(email)) {
            return "This email is already in use! Please write another one."
                    + "\nOtherwise cancel registration by " + CANCEL;
        }

        appUser.setEmail(email);
        appUser.setState(BASIC_STATE);
        appUser.setIsActive(false);
        appUserRepository.save(appUser);

        answerProducer.produceAnswer("Email successfully added."
                + " Sending activation message to provided email...",
                appUser.getTelegramChatId());

        try {
            return mailServiceClient.sendMailActivationRequest(appUser, email);
        } catch (Exception e) {
            log.error("Mail activation failure"
                    + " while sending request to MailService", e);
            return "Can't send mail activation message. Please try later";
        }
    }

    private String handleEmailOperation(AppUser appUser) {
        appUser.setState(WAIT_FOR_EMAIL_STATE);
        appUserRepository.save(appUser);
        return "Please write your email"
                + "\nOtherwise cancel this operation by " + CANCEL;
    }

    private boolean isAvailableEmail(String email) {
        Optional<AppUser> userByEmail = appUserRepository.findByEmail(email);
        return userByEmail.map(appUser -> !appUser.getIsActive()).orElse(true);
    }
}
