package org.example.repository;

import org.example.model.AppDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppDocRepository extends JpaRepository<AppDoc, Long> {
}
