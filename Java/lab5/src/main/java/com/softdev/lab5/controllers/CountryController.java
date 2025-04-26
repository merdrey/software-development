package com.softdev.lab5.controllers;

import com.softdev.lab5.models.Artist;
import com.softdev.lab5.models.Country;
import com.softdev.lab5.repositories.CountryRepo;
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

    @GetMapping("/countries/{id}/artists")
    public ResponseEntity<List<Artist>> getCountryArtists(@PathVariable(value = "id") Long countryId) {
        Optional<Country> cc = repo.findById(countryId);
        if (cc.isPresent()) {
            return ResponseEntity.ok(cc.get().artists);
        }
        return ResponseEntity.ok(new ArrayList<Artist>());
    }

    @PostMapping("/countries")
    public ResponseEntity<Object> createCountry(@RequestBody Country country)
    throws Exception {
        try {
            return new ResponseEntity<Object>(repo.save(country), HttpStatus.CREATED);
        }
        catch (Exception ex) {
            String error;
            if (ex.getMessage().contains("uqname")) {
                error = "alreadyExistCountryError";
            }
            else if (ex.getMessage().contains("NOT NULL")) {
                error = "emptyCountryNameError";
            }
            else {
                error = "undefinedError";
            }
            Map<String, String> map = new HashMap<>();
            map.put("error", error);
            return new ResponseEntity<Object>(map, HttpStatus.OK);
        }
    }

    @PutMapping("/countries/{id}")
    public ResponseEntity<Object> updateCountry(@RequestBody Country countryDetails, @PathVariable(value = "id") long id) {
        Country country = null;
        Optional<Country> cc = repo.findById(id);
        if (cc.isPresent()) {
            country = cc.get();
            country.name = countryDetails.name;
            repo.save(country);
            return new ResponseEntity<Object>(country, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "country not found");
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
}
