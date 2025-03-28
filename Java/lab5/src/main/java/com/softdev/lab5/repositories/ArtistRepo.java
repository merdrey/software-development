package com.softdev.lab5.repositories;

import com.softdev.lab5.models.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepo extends JpaRepository<Artist, Long> {
}
