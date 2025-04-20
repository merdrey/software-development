package com.softdev.lab5.repositories;

import com.softdev.lab5.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    @Transactional
    @Query(value = "select u from User u where u.token = ?1")
    Optional<User> findByToken(String token);
    @Transactional
    @Query(value = "select u from User u where u.login = ?1")
    Optional<User> findByLogin(String login);
}
