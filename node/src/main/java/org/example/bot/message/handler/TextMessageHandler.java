package org.example.bot.message.handler;

import static org.example.enums.BotCommand.CANCEL;
import static org.example.enums.BotCommand.fromValue;
import static org.example.model.enums.UserState.BASIC_STATE;
import static org.example.model.enums.UserState.WAIT_FOR_EMAIL_STATE;

import lombok.extern.log4j.Log4j;
import org.example.bot.command.CommandsDispatcher;
import org.example.bot.message.MessageHandler;
import org.example.enums.BotCommand;
import org.example.model.AppUser;
import org.example.model.enums.UserState;
import org.example.repository.AppUserRepository;
import org.example.repository.RawDataRepository;
import org.example.service.AnswerProducer;
import org.example.service.AppUserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@Service
public class TextMessageHandler extends MessageHandler {

    private final AppUserService appUserService;

    private final CommandsDispatcher commandsDispatcher;

    public TextMessageHandler(
            AnswerProducer answerProducer,
            RawDataRepository rawDataRepository,
            AppUserRepository appUserRepository,
            AppUserService appUserService,
            CommandsDispatcher commandsDispatcher
    ) {
        super(appUserRepository, answerProducer, rawDataRepository);
        this.appUserService = appUserService;
        this.commandsDispatcher = commandsDispatcher;
    }

    @Override
    public void handleMessage(Update update) {
        saveRawData(update);
        Message message = update.getMessage();
        AppUser appUser = saveOrGetAppUser(message);
        UserState userState = appUser.getState();

        String output = "";
        BotCommand command = fromValue(message.getText());
        if (command == CANCEL) {
            output = handleCancelling(appUser);
        } else if (userState == BASIC_STATE) {
            output = commandsDispatcher.dispatchToHandler(command, appUser);
        } else if (userState == WAIT_FOR_EMAIL_STATE) {
            output = appUserService.setEmail(message.getText(), appUser);
        } else {
            log.error("Unknown userState" + userState);
            output = "Unknown error. Write " + CANCEL + " and try again";
        }
        answerProducer.produceAnswer(output, appUser.getTelegramChatId());
    }

    private String handleCancelling(AppUser appUser) {
        appUser.setState(BASIC_STATE);
        appUserRepository.save(appUser);
        return "Current operation is cancelled";
    }
}
