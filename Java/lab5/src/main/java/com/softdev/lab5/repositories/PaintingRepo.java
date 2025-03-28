package com.softdev.lab5.repositories;

import com.softdev.lab5.models.Painting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaintingRepo extends JpaRepository<Painting, Long> {
}
