package org.example.bot.message;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageHandler {
    void handleMessage(Update update);

    boolean isSupportedMessage(Message message);
}
