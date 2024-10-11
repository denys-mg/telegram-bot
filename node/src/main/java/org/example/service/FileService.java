package org.example.service;

import org.example.enums.LinkType;
import org.example.model.AppDoc;
import org.example.model.AppPhoto;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

public interface FileService {
    void checkFileSize(Long fileSize);

    byte[] getFileInByte(String fileId);

    AppDoc uploadDocument(Document document, byte[] fileInBytes);

    AppPhoto uploadPhoto(PhotoSize photo, byte[] fileInBytes);

    String generateLink(Long fileId, LinkType linkType);
}
