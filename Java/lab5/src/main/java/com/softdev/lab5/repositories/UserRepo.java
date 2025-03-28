package com.softdev.lab5.repositories;

import com.softdev.lab5.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
