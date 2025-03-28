package com.softdev.lab5.models;

import jakarta.persistence.*;

@Entity
@Table(name = "countries")
@Access(AccessType.FIELD)
public class Country {
    public Country() {

    }

    public Country(long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long id;

    @Column(name = "name")
    public String name;
}
