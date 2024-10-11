package org.example.service;

import org.example.model.AppDoc;
import org.example.model.AppPhoto;

public interface FileService {
    AppDoc getDocument(String id);

    AppPhoto getPhoto(String id);
}
