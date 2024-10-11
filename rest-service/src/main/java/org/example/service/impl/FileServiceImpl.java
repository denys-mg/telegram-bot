package org.example.service.impl;

import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.example.model.AppDoc;
import org.example.model.AppPhoto;
import org.example.repository.AppDocRepository;
import org.example.repository.AppPhotoRepository;
import org.example.service.FileService;
import org.example.util.CryptoTool;
import org.springframework.stereotype.Service;

@Log4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final CryptoTool cryptoTool;

    private final AppDocRepository appDocRepository;

    private final AppPhotoRepository appPhotoRepository;

    @Override
    public AppDoc getDocument(String encodedId) {
        Long docId = cryptoTool.idOf(encodedId);
        return appDocRepository.findById(docId)
                .orElseThrow(() -> new NoSuchElementException(
                        "There are no such document with given id: " + encodedId));
    }

    @Override
    public AppPhoto getPhoto(String encodedId) {
        Long photoId = cryptoTool.idOf(encodedId);
        return appPhotoRepository.findById(photoId)
                .orElseThrow(() -> new NoSuchElementException(
                        "There are no such photo with given id: " + encodedId));
    }
}
