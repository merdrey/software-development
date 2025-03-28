package com.softdev.lab5.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Access(AccessType.FIELD)
public class User {
    public User() {

    }

    public User(long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long id;
    @Column(name = "login")
    public String login;
    @JsonIgnore
    @Column(name = "password")
    public String password;
    @Column(name = "email")
    public String email;
    @JsonIgnore
    @Column(name = "salt")
    public String salt;
    @Column(name = "token")
    public String token;
    @Column(name = "activity")
    public LocalDateTime activity;
    @ManyToMany(mappedBy = "users")
    public Set<Museum> museums = new HashSet<>();

    public void addMuseum(Museum m) {
        this.museums.add(m);
        m.users.add(this);
    }

    public void removeMuseum(Museum m) {
        this.museums.remove(m);
        m.users.remove(this);
    }
}
