package com.softdev.lab5.models;

import jakarta.persistence.*;

@Entity
@Table(name = "artists")
@Access(AccessType.FIELD)
public class Artist {
    public Artist() {

    }
    public Artist(long id) {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long id;
    @Column(name = "name")
    public String name;
    @Column(name = "age")
    public String age;
    @ManyToOne()
    @JoinColumn(name = "countryid")
    public Country country;
}
