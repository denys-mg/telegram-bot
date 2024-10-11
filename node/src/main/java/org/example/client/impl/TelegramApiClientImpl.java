package org.example.client.impl;

import java.util.List;
import org.example.client.TelegramApiClient;
import org.example.dto.response.TelegramApiResponseDto;
import org.example.exception.UploadFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

@Component
public class TelegramApiClientImpl implements TelegramApiClient {

    private static final int MAX_DOWNLOAD_ATTEMPTS = 2;

    private static final int CHUNK_SIZE = 5 * 1024 * 1024;

    private final String botToken;

    private final String fileInfoUrl;

    private final String fileStorageUrl;

    private final RestTemplate restTemplate;

    public TelegramApiClientImpl(
            @Value("${bot.token}") String botToken,
            @Value("${bot.api.file-info-request.link}") String fileInfoUrl,
            @Value("${bot.api.file-storage.link}") String fileStorageUrl,
            RestTemplate restTemplate
    ) {
        this.botToken = botToken;
        this.fileInfoUrl = fileInfoUrl;
        this.fileStorageUrl = fileStorageUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public TelegramApiResponseDto.Result getFileInfo(String fileId) {
        TelegramApiResponseDto response = restTemplate.getForObject(
                fileInfoUrl,
                TelegramApiResponseDto.class,
                botToken, fileId
        );

        if (response != null && response.isResponseSuccessful()
                && response.result() != null) {
            return response.result();
        }
        throw new UploadFileException("Unable to get file information."
                + " Bad response from telegram API " + response);
    }

    @Override
    public byte[] downloadFile(String filePath, int fileSize) {
        Assert.notNull(filePath, "Path to file can't be null!");
        Assert.state(fileSize != 0, "File size is invalid - " + fileSize);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> request = new HttpEntity<>(headers);

        int downloadedBytes = 0;
        int downloadAttempts = MAX_DOWNLOAD_ATTEMPTS;
        byte[] file = new byte[fileSize];
        int startBytePos = 0;
        int endBytePos = CHUNK_SIZE;

        while (downloadedBytes < fileSize) {
            headers.setRange(List.of(HttpRange.createByteRange(startBytePos, endBytePos)));
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    fileStorageUrl,
                    HttpMethod.GET,
                    request,
                    byte[].class,
                    botToken, filePath
            );

            HttpStatus responseStatusCode = response.getStatusCode();
            byte[] responseBody = response.getBody();

            if (responseBody == null || responseBody.length == 0) {
                throw new UploadFileException("Nothing to download"
                        + " as response body is null or empty");
            }
            if (responseStatusCode == HttpStatus.PARTIAL_CONTENT
                    || responseStatusCode == HttpStatus.OK) {
                System.arraycopy(
                        responseBody, 0,
                        file, startBytePos,
                        responseBody.length);
                startBytePos = endBytePos + 1;
                endBytePos = Math.min(startBytePos + CHUNK_SIZE, fileSize);
                downloadedBytes += responseBody.length;
                downloadAttempts = MAX_DOWNLOAD_ATTEMPTS;
            } else if (responseStatusCode.is5xxServerError()) {
                throw new UploadFileException("A file could not be downloaded"
                        + " from Telegram Storage! Please try again later");
            } else {
                --downloadAttempts;
            }

            if (downloadAttempts == 0) {
                throw new UploadFileException("A file could not be downloaded"
                        + " from Telegram Storage! Number of attempts are exceeded");
            }
        }
        return file;
    }
}
