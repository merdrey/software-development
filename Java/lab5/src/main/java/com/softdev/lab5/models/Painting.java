package com.softdev.lab5.models;

import jakarta.persistence.*;

@Entity
@Table(name = "paintings")
@Access(AccessType.FIELD)
public class Painting {
    public Painting() {

    }

    public Painting(long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long id;
    @Column(name = "name")
    public String name;
    @OneToOne
    @JoinColumn(name = "artistid")
    public Artist artist;
    @OneToOne
    @JoinColumn(name = "museumid")
    public Museum museum;
    public long year;
}
