package org.example.bot.message;

import static org.example.enums.BotCommand.CANCEL;
import static org.example.enums.BotCommand.REGISTRATION;
import static org.example.model.enums.UserState.BASIC_STATE;

import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.model.AppUser;
import org.example.model.RawData;
import org.example.model.enums.UserState;
import org.example.repository.AppUserRepository;
import org.example.repository.RawDataRepository;
import org.example.service.AnswerProducer;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@RequiredArgsConstructor
public abstract class MessageHandler {

    protected final AppUserRepository appUserRepository;

    protected final AnswerProducer answerProducer;

    protected final RawDataRepository rawDataRepository;

    public abstract void handleMessage(Update update);

    protected boolean isNotAllowToSendContent(AppUser appUser) {
        UserState userState = appUser.getState();
        if (!appUser.getIsActive()) {
            String errorMsg = "To send any content, you need to be registered "
                    + "and activate your account! " + REGISTRATION;
            answerProducer.produceAnswer(errorMsg, appUser.getTelegramChatId());
            return true;
        } else if (userState != BASIC_STATE) {
            String errorMsg = "Firstly cancel current operation - " + CANCEL;
            answerProducer.produceAnswer(errorMsg, appUser.getTelegramChatId());
            return true;
        }
        return false;
    }

    protected void sendContentProcessingAnswer(Message message) {
        if (message.getMediaGroupId() == null) {
            answerProducer.produceAnswer("File is received. Processing...",
                    message.getChatId());
        }
    }

    protected void saveRawData(Update update) {
        rawDataRepository.save(new RawData(update));
    }

    protected AppUser saveOrGetAppUser(Message message) {
        User telegramUser = message.getFrom();
        Long chatId = message.getChatId();
        Optional<AppUser> persistentAppUser = appUserRepository
                .findByTelegramUserId(telegramUser.getId());

        if (persistentAppUser.isEmpty()) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .telegramChatId(chatId)
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    .username(telegramUser.getUserName())
                    .isActive(false)
                    .state(BASIC_STATE)
                    .build();
            return appUserRepository.save(transientAppUser);
        }

        AppUser appUser = persistentAppUser.get();
        if (!Objects.equals(appUser.getTelegramChatId(), chatId)) {
            appUser.setTelegramChatId(chatId);
            return appUserRepository.save(appUser);
        }
        return appUser;
    }
}
