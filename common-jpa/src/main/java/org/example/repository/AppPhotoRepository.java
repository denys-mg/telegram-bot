package org.example.repository;

import org.example.model.AppPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppPhotoRepository extends JpaRepository<AppPhoto, Long> {
}
