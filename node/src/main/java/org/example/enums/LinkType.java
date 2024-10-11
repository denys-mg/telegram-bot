package org.example.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum LinkType {

    GET_DOC("file/get-doc"),
    GET_PHOTO("file/get-photo");

    private final String link;

    @Override
    public String toString() {
        return link;
    }
}
