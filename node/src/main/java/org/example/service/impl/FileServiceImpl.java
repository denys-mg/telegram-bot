package org.example.service.impl;

import lombok.extern.log4j.Log4j;
import org.example.client.TelegramApiClient;
import org.example.dto.response.TelegramApiResponseDto;
import org.example.enums.LinkType;
import org.example.exception.UploadFileException;
import org.example.model.AppDoc;
import org.example.model.AppPhoto;
import org.example.model.BinaryContent;
import org.example.repository.AppDocRepository;
import org.example.repository.AppPhotoRepository;
import org.example.repository.BinaryContentRepository;
import org.example.service.FileService;
import org.example.util.CryptoTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

@Log4j
@Service
public class FileServiceImpl implements FileService {

    private static final int SUPPORTED_FILE_SIZE_MB = 20;

    private static final int SUPPORTED_FILE_SIZE_BYTES =
            SUPPORTED_FILE_SIZE_MB * 1024 * 1024;

    private final String restServerAddress;

    private final CryptoTool cryptoTool;

    private final TelegramApiClient telegramApiClient;

    private final AppDocRepository appDocRepository;

    private final AppPhotoRepository appPhotoRepository;

    private final BinaryContentRepository binaryContentRepository;

    public FileServiceImpl(
            @Value("${service.rest.url}")
            String restServerAddress,
            CryptoTool cryptoTool,
            TelegramApiClient telegramApiClient,
            AppDocRepository appDocRepository,
            AppPhotoRepository appPhotoRepository,
            BinaryContentRepository binaryContentRepository
    ) {
        this.restServerAddress = restServerAddress;
        this.cryptoTool = cryptoTool;
        this.telegramApiClient = telegramApiClient;
        this.appDocRepository = appDocRepository;
        this.appPhotoRepository = appPhotoRepository;
        this.binaryContentRepository = binaryContentRepository;
    }

    @Override
    public void checkFileSize(Long fileSize) {
        if (fileSize > SUPPORTED_FILE_SIZE_BYTES) {
            throw new UploadFileException("Document size is too large!"
                    + " Will only work with files of up to "
                    + SUPPORTED_FILE_SIZE_MB + " MB in size");
        }
    }

    @Override
    public byte[] getFileInByte(String fileId) {
        TelegramApiResponseDto.Result fileInfo =
                telegramApiClient.getFileInfo(fileId);

        return telegramApiClient.downloadFile(
                fileInfo.filePath(), fileInfo.fileSize());
    }

    @Transactional
    @Override
    public AppDoc uploadDocument(Document doc, byte[] fileInByte) {
        BinaryContent persistentBinaryContent =
                binaryContentRepository.save(new BinaryContent(fileInByte));
        return saveAppDoc(doc, persistentBinaryContent);
    }

    @Transactional
    @Override
    public AppPhoto uploadPhoto(PhotoSize photo, byte[] fileInByte) {
        BinaryContent persistentBinaryContent =
                binaryContentRepository.save(new BinaryContent(fileInByte));
        return saveAppPhoto(photo, persistentBinaryContent);
    }

    @Override
    public String generateLink(Long fileId, LinkType linkType) {
        String hashedFileId = cryptoTool.hashOf(fileId);
        return restServerAddress + "/"
                + linkType.toString() + "?fileId=" + hashedFileId;
    }

    private AppDoc saveAppDoc(Document telegramDoc, BinaryContent binaryContent) {
        AppDoc transientAppDoc = AppDoc.builder()
                .telegramFileId(telegramDoc.getFileId())
                .docName(telegramDoc.getFileName())
                .fileSize(Math.toIntExact(telegramDoc.getFileSize()))
                .mimeType(telegramDoc.getMimeType())
                .binaryContent(binaryContent)
                .build();
        return appDocRepository.save(transientAppDoc);
    }

    private AppPhoto saveAppPhoto(PhotoSize telegramPhoto, BinaryContent binaryContent) {
        AppPhoto transientAppPhoto = AppPhoto.builder()
                .telegramFileId(telegramPhoto.getFileId())
                .fileSize(telegramPhoto.getFileSize())
                .binaryContent(binaryContent)
                .build();
        return appPhotoRepository.save(transientAppPhoto);
    }
}
