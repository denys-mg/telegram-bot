package org.example.client;

import org.example.model.AppUser;

public interface MailServiceClient {
    String sendMailActivationRequest(AppUser appUser, String email);
}
