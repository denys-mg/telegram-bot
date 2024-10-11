package org.example.controller;

import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Log4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final String botName;

    private final String botToken;

    private final UpdateController updateController;

    public TelegramBot(
            @Value("${bot.name}")
            String botName,
            @Value("${bot.token}")
            String botToken,
            UpdateController updateController
    ) {
        this.botName = botName;
        this.botToken = botToken;
        this.updateController = updateController;
    }

    @PostConstruct
    public void init() {
        updateController.registerBot(this);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateController.processUpdate(update);
    }

    public void sendAnswerMessage(SendMessage answer) {
        if (answer != null) {
            try {
                execute(answer);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        } else {
            log.error("Can't send message as answer is null");
        }
    }
}
