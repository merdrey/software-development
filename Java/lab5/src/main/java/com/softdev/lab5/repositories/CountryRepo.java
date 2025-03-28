package com.softdev.lab5.repositories;

import com.softdev.lab5.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepo extends JpaRepository<Country, Long> {

}
