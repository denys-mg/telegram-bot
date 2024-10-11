package org.example.messaging.consumer;

import static org.example.enums.RabbitQueue.DOC_MESSAGE_UPDATE;
import static org.example.enums.RabbitQueue.PHOTO_MESSAGE_UPDATE;
import static org.example.enums.RabbitQueue.TEXT_MESSAGE_UPDATE;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.example.bot.message.MessageHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@Component
@RequiredArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {

    private final Map<String, MessageHandler> messageHandlers;

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumeTextMessageUpdates(Update update) {
        log.debug("NODE: text message is received");
        messageHandlers.get("textMessageHandler").handleMessage(update);
    }

    @Override
    @RabbitListener(queues = DOC_MESSAGE_UPDATE)
    public void consumeDocMessageUpdates(Update update) {
        log.debug("NODE: document message is received");
        messageHandlers.get("docMessageHandler").handleMessage(update);
    }

    @Override
    @RabbitListener(queues = PHOTO_MESSAGE_UPDATE)
    public void consumePhotoMessageUpdates(Update update) {
        log.debug("NODE: photo message is received");
        messageHandlers.get("photoMessageHandler").handleMessage(update);
    }
}
