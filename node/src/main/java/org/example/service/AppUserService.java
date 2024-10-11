package org.example.service;

import org.example.model.AppUser;

public interface AppUserService {
    String setEmail(String message, AppUser appUser);

    String handleRegistration(AppUser appUser);

    String handleEmailChange(AppUser appUser);
}
