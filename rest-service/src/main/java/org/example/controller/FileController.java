package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.example.model.AppDoc;
import org.example.model.AppPhoto;
import org.example.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private static final String PHOTO_DOWNLOAD_NAME = "photo.jpg";

    private final FileService fileService;

    @GetMapping("/get-doc")
    public ResponseEntity<byte[]> getDocument(
            @RequestParam("fileId") String docId
    ) {
        try {
            AppDoc doc = fileService.getDocument(docId);
            return ResponseEntity
                    .ok()
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + doc.getDocName() + "\"")
                    .contentType(MediaType.parseMediaType(doc.getMimeType()))
                    .body(doc.getBinaryContent().getBytes());
        } catch (Exception ex) {
            log.error(ex);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get-photo")
    public ResponseEntity<byte[]> getPhoto(
            @RequestParam("fileId") String photoId
    ) {
        try {
            AppPhoto photo = fileService.getPhoto(photoId);
            return ResponseEntity
                    .ok()
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + PHOTO_DOWNLOAD_NAME + "\"")
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(photo.getBinaryContent().getBytes());
        } catch (Exception ex) {
            log.error(ex);
            return ResponseEntity.notFound().build();
        }
    }
}
