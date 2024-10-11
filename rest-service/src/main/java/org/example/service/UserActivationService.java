package org.example.service;

import org.example.model.AppUser;

public interface UserActivationService {
    AppUser getAppUser(String encodedId);

    void updateAppUserIsActive(AppUser appUser, boolean isActive);
}
