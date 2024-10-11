package org.example.bot.message.handler;

import lombok.extern.log4j.Log4j;
import org.example.bot.message.MessageHandler;
import org.example.enums.LinkType;
import org.example.exception.UploadFileException;
import org.example.model.AppDoc;
import org.example.model.AppUser;
import org.example.repository.AppUserRepository;
import org.example.repository.RawDataRepository;
import org.example.service.AnswerProducer;
import org.example.service.FileService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@Service
public class DocMessageHandler extends MessageHandler {

    private final FileService fileService;

    public DocMessageHandler(
            AnswerProducer answerProducer,
            RawDataRepository rawDataRepository,
            AppUserRepository appUserRepository,
            FileService fileService) {
        super(appUserRepository, answerProducer, rawDataRepository);
        this.fileService = fileService;
    }

    @Override
    public void handleMessage(Update update) {
        Message message = update.getMessage();
        AppUser appUser = saveOrGetAppUser(message);
        if (isNotAllowToSendContent(appUser)) {
            return;
        }
        sendContentProcessingAnswer(message);
        saveRawData(update);

        String output;
        try {
            Document telegramDoc = message.getDocument();
            fileService.checkFileSize(telegramDoc.getFileSize());
            byte[] fileInBytes = fileService.getFileInByte(telegramDoc.getFileId());
            AppDoc appDoc = fileService.uploadDocument(telegramDoc, fileInBytes);
            String link = fileService.generateLink(appDoc.getId(), LinkType.GET_DOC);
            output = "Document successfully saved! "
                    + "Here is link for downloading - "
                    + "<a href=\"" + link + "\">Download</a>";
        } catch (UploadFileException ex) {
            log.error(ex);
            output = ex.getMessage();
        } catch (Exception ex) {
            log.error(ex);
            output = "Unable to upload this file";
        }
        answerProducer.produceAnswerHtml(output, appUser.getTelegramChatId());
    }
}
