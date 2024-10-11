package org.example.bot.message.handler;

import static org.example.enums.RabbitQueue.TEXT_MESSAGE_UPDATE;

import lombok.RequiredArgsConstructor;
import org.example.bot.message.MessageHandler;
import org.example.messaging.producer.UpdateProducer;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class TextMessageHandler implements MessageHandler {

    private final UpdateProducer updateProducer;

    @Override
    public void handleMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }

    @Override
    public boolean isSupportedMessage(Message message) {
        return message.hasText();
    }
}
