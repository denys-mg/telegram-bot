package org.example.client;

import org.example.dto.response.TelegramApiResponseDto;

public interface TelegramApiClient {
    TelegramApiResponseDto.Result getFileInfo(String fileId);

    byte[] downloadFile(String filePath, int fileSize);
}
