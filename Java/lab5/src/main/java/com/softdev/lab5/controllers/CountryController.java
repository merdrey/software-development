package com.softdev.lab5.controllers;

import com.softdev.lab5.models.Artist;
import com.softdev.lab5.models.Country;
import com.softdev.lab5.repositories.CountryRepo;
import com.softdev.lab5.tools.DataValidationException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class CountryController {

    @Autowired
    CountryRepo repo;

    @GetMapping("/countries")
    public List<Country> getAllCountries() {
        return repo.findAll();
    }

    @GetMapping("/countries/{id}")
    public ResponseEntity<Country> getCountry(@PathVariable(value = "id") Long countryId) throws DataValidationException {
        Optional<Country> cc = Optional.ofNullable(repo.findById(countryId)
                .orElseThrow(() -> new DataValidationException("Страна с таким индексом не найдена")));
        if (cc.isPresent()) {
            return ResponseEntity.ok(cc.get());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/countries/{id}/artists")
    public ResponseEntity<List<Artist>> getCountryArtists(@PathVariable(value = "id") Long countryId) throws DataValidationException {
        Optional<Country> cc = Optional.ofNullable(repo.findById(countryId)
                .orElseThrow(() -> new DataValidationException("Страна с таким индексом не найдена")));
        if (cc.isPresent()) {
            return ResponseEntity.ok(cc.get().artists);
        }
        return ResponseEntity.ok(new ArrayList<Artist>());
    }

    @PostMapping("/countries")
    public ResponseEntity<Country> createCountry(@RequestBody Country country)
    throws DataValidationException {
        try {
            return new ResponseEntity<>(repo.save(country), HttpStatus.CREATED);
        }
        catch (Exception ex) {
            if (ex.getMessage().contains("uqname"))
                throw new DataValidationException("Эта страна уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }

    @PutMapping("/countries/{id}")
    public ResponseEntity<Object> updateCountry(@RequestBody Country countryDetails, @PathVariable(value = "id") long id)
    throws DataValidationException{
        try {
            Country country = repo.findById(id)
                    .orElseThrow(() -> new DataValidationException("Страна с таким индексом не найдена"));
            country.name = countryDetails.name;
            repo.save(country);
            return new ResponseEntity<Object>(country, HttpStatus.OK);

        }
        catch (Exception ex) {
            if (ex.getMessage().contains("uqname"))
                throw new DataValidationException("Эта страна уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }

    @DeleteMapping("/countries/{id}")
    public ResponseEntity<Object> deleteCountry(@PathVariable(value = "id") Long countryId) {
        Optional<Country> country = repo.findById(countryId);
        Map<String, Boolean> resp = new HashMap<>();
        if (country.isPresent()) {
            repo.delete(country.get());
            resp.put("deleted", Boolean.TRUE);
        }
        else
            resp.put("deleted", Boolean.FALSE);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/deleteCountries")
    public ResponseEntity<Object> deleteCountries() {
        repo.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
