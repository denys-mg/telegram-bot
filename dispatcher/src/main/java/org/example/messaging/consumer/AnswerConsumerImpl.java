package org.example.messaging.consumer;

import static org.example.enums.RabbitQueue.ANSWER_MESSAGE;

import lombok.RequiredArgsConstructor;
import org.example.controller.UpdateController;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@RequiredArgsConstructor
public class AnswerConsumerImpl implements AnswerConsumer {

    private final UpdateController updateController;

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        updateController.sendView(sendMessage);
    }
}
