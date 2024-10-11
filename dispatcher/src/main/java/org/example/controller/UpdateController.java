package org.example.controller;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.example.bot.message.MessageHandler;
import org.example.bot.message.MessageHandlerStrategy;
import org.example.enums.SupportedMessageType;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@Component
@RequiredArgsConstructor
public class UpdateController {

    private final MessageHandlerStrategy messageStrategy;

    private TelegramBot telegramBot;

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

    public void sendView(Update update, String text) {
        SendMessage answer = new SendMessage();
        answer.setChatId(update.getMessage().getChatId());
        answer.setText(text);
        telegramBot.sendAnswerMessage(answer);
    }

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("Received update is null");
            return;
        }

        if (update.hasMessage()) {
            distributeMessageByType(update);
        } else {
            log.error("Unsupported update is received " + update);
        }
    }

    private void distributeMessageByType(Update update) {
        Optional<MessageHandler> messageHandler = messageStrategy
                .getHandler(update.getMessage());

        if (messageHandler.isPresent()) {
            messageHandler.get().handleMessage(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }
    }

    private void setUnsupportedMessageTypeView(Update update) {
        String supportedMessageTypes =
                Arrays.stream(SupportedMessageType.values())
                .map(SupportedMessageType::toString)
                .collect(Collectors.joining(", "));
        sendView(update, "Unsupported message type,"
                + " required only: " + supportedMessageTypes);
    }
}
