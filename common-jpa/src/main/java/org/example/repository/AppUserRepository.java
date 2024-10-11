package org.example.repository;

import java.util.Optional;
import org.example.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByTelegramUserId(Long userId);

    Optional<AppUser> findByEmail(String email);
}
