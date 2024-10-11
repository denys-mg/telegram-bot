package org.example.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.model.AppUser;
import org.example.repository.AppUserRepository;
import org.example.service.UserActivationService;
import org.example.util.CryptoTool;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserActivationServiceImpl implements UserActivationService {

    private final AppUserRepository appUserRepository;

    private final CryptoTool cryptoTool;

    @Override
    public AppUser getAppUser(String encodedId) {
        Long id = cryptoTool.idOf(encodedId);
        Optional<AppUser> persistentAppUser = appUserRepository.findById(id);
        return persistentAppUser.orElseThrow(
                () -> new NoSuchElementException(
                        "There are no user with provided id: " + encodedId));
    }

    @Override
    public void updateAppUserIsActive(AppUser appUser, boolean isActive) {
        appUser.setIsActive(isActive);
        appUserRepository.save(appUser);
    }
}
