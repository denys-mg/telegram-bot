package org.example.bot.message;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class MessageHandlerStrategy {

    private final List<MessageHandler> messageHandlers;

    public Optional<MessageHandler> getHandler(Message message) {
        return messageHandlers.stream()
                .filter(handler -> handler.isSupportedMessage(message))
                .findAny();
    }
}
