package org.example.service;

public interface AnswerProducer {
    void produceAnswer(String answerMessage, Long chatId);

    void produceAnswerHtml(String answerMessage, Long chatId);
}
