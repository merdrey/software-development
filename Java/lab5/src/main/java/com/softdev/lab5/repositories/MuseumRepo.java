package com.softdev.lab5.repositories;

import com.softdev.lab5.models.Museum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MuseumRepo extends JpaRepository<Museum, Long> {
}
