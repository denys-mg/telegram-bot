package org.example.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SupportedMessageType {

    TEXT_MESSAGE("text"),
    DOCUMENT_MESSAGE("document"),
    PHOTO_MESSAGE("photo");

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
