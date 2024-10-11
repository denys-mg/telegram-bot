package org.example.bot.message.handler;

import lombok.extern.log4j.Log4j;
import org.example.bot.message.MessageHandler;
import org.example.enums.LinkType;
import org.example.exception.UploadFileException;
import org.example.model.AppPhoto;
import org.example.model.AppUser;
import org.example.repository.AppUserRepository;
import org.example.repository.RawDataRepository;
import org.example.service.AnswerProducer;
import org.example.service.FileService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@Service
public class PhotoMessageHandler extends MessageHandler {

    private final FileService fileService;

    public PhotoMessageHandler(
            AnswerProducer answerProducer,
            RawDataRepository rawDataRepository,
            AppUserRepository appUserRepository,
            FileService fileService
    ) {
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

        String output = "";
        try {
            PhotoSize telegramPhoto = getPhotoSize(message);
            fileService.checkFileSize(Long.valueOf(telegramPhoto.getFileSize()));
            byte[] photoInBytes = fileService.getFileInByte(telegramPhoto.getFileId());
            AppPhoto appPhoto = fileService.uploadPhoto(telegramPhoto, photoInBytes);
            String link = fileService.generateLink(appPhoto.getId(), LinkType.GET_PHOTO);
            output = "Photo successfully saved! "
                    + "Here is link for downloading - "
                    + "<a href=\"" + link + "\">Download</a>";
        } catch (UploadFileException ex) {
            log.error(ex);
            output = ex.getMessage();
        } catch (Exception ex) {
            log.error(ex);
            output = "Unable to upload this photo";
        }
        answerProducer.produceAnswerHtml(output, appUser.getTelegramChatId());
    }

    private PhotoSize getPhotoSize(Message message) {
        int photoSizesCount = message.getPhoto().size();
        int photoSizeIdx = photoSizesCount > 0
                ? photoSizesCount - 1
                : 0;
        return message.getPhoto().get(photoSizeIdx);
    }
}
