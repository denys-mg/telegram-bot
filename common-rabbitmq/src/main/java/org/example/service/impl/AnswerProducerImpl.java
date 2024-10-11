package org.example.service.impl;

import static org.example.enums.RabbitQueue.ANSWER_MESSAGE;

import lombok.RequiredArgsConstructor;
import org.example.service.AnswerProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
public class AnswerProducerImpl implements AnswerProducer {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void produceAnswer(String answerMessage, Long chatId) {
        rabbitTemplate.convertAndSend(
                ANSWER_MESSAGE,
                createSendMessage(answerMessage, chatId));
    }

    @Override
    public void produceAnswerHtml(String answerMessage, Long chatId) {
        SendMessage sendMessage = createSendMessage(answerMessage, chatId);
        sendMessage.enableHtml(true);
        rabbitTemplate.convertAndSend(ANSWER_MESSAGE, sendMessage);
    }

    private SendMessage createSendMessage(String answerMessage, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(answerMessage);
        return sendMessage;
    }
}
