package com.softdev.lab5.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.softdev.lab5.tools.View;
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
    @JsonView(View.Rest.class)
    @Column(name = "id")
    public long id;
    @JsonView(View.Rest.class)
    @Column(name = "login")
    public String login;
    @JsonIgnore
    @Column(name = "password")
    public String password;
    @JsonView(View.Rest.class)
    @Column(name = "email")
    public String email;
    @JsonIgnore
    @Column(name = "salt")
    public String salt;
    @JsonView(View.Login.class)
    @Column(name = "token")
    public String token;
    @JsonView(View.Rest.class)
    @Column(name = "activity")
    public LocalDateTime activity;
    @JsonView(View.Rest.class)
    @ManyToMany(mappedBy = "users")
    public Set<Museum> museums = new HashSet<>();
    @Transient
    public String np;
    public void addMuseum(Museum m) {
        this.museums.add(m);
        m.users.add(this);
    }

    public void removeMuseum(Museum m) {
        this.museums.remove(m);
        m.users.remove(this);
    }
}
