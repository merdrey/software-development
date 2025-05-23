package com.softdev.lab5.controllers;

import com.softdev.lab5.models.Artist;
import com.softdev.lab5.models.Country;
import com.softdev.lab5.repositories.ArtistRepo;
import com.softdev.lab5.repositories.CountryRepo;
import com.softdev.lab5.tools.DataValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class ArtistController {

    @Autowired
    ArtistRepo repo;
    @Autowired
    CountryRepo countryRepo;

    @GetMapping("/artists")
    public Page<Artist> getAllArtists(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return repo.findAll(PageRequest.of(page, limit).withSort(Sort.by(Sort.Direction.ASC, "name")));
    }

    @GetMapping("/artists/{id}")
    public ResponseEntity<Artist> getArtist(@PathVariable(value = "id") Long artistId)
            throws DataValidationException
    {
        Artist artist = repo.findById(artistId)
                .orElseThrow(()-> new DataValidationException("Художник с таким индексом не найден"));
        return ResponseEntity.ok(artist);
    }

    @PostMapping("/artists")
    public ResponseEntity<Object> createArtist(@RequestBody Artist art) throws DataValidationException{
        try {
            return new ResponseEntity<>(repo.save(art), HttpStatus.CREATED);
        }
        catch (Exception ex) {
            if (ex.getMessage().contains("uqname"))
                throw new DataValidationException("Этот художник уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }

    @PutMapping("/artists/{id}")
    public ResponseEntity<Object> updateArtist(@RequestBody Artist artistDetails, @PathVariable(value = "id") long id) throws DataValidationException {
        try {
            Artist artist = repo.findById(id)
                    .orElseThrow(() -> new DataValidationException("Художник с таким индексом не найден"));
            artist.name = artistDetails.name;
            artist.age = artistDetails.age;
            artist.country = artistDetails.country;
            return new ResponseEntity<Object>(repo.save(artist), HttpStatus.OK);

        }
        catch (Exception ex) {
            if (ex.getMessage().contains("uqname"))
                throw new DataValidationException("Этот художник уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }

    @PostMapping("/deleteArtists")
    public ResponseEntity<Object> deleteArtists(@RequestBody List<Artist> artists) {
        repo.deleteAll(artists);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
