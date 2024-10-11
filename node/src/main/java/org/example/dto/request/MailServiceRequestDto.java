package org.example.dto.request;

public record MailServiceRequestDto(
        String userId,
        String mailTo
) {}
