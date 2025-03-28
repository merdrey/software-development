package com.softdev.lab5.controllers;

import com.softdev.lab5.models.Artist;
import com.softdev.lab5.models.Country;
import com.softdev.lab5.repositories.ArtistRepo;
import com.softdev.lab5.repositories.CountryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class ArtistController {

    @Autowired
    ArtistRepo repo;
    @Autowired
    CountryRepo countryRepo;

    @GetMapping("/artists")
    public List<Artist> getAllArtists() {
        return repo.findAll();
    }

    @PostMapping("/artists")
    public ResponseEntity<Object> createArtist(@RequestBody Artist art) throws Exception{
        try {
            Optional<Country> cc = countryRepo.findById(art.country.id);
            if (cc.isPresent()) {
                art.country = cc.get();
            }
            return new ResponseEntity<>(repo.save(art), HttpStatus.OK);
        }
        catch (Exception ex) {
            String error;
            if (ex.getMessage().contains("uqartistname")) {
                error = "alreadyExistArtistError";
            }
            else if (ex.getMessage().contains("NOT NULL")) {
                error = "emptyArtistNameError";
            }
            else {
                error = "undefinedError";
            }
            Map<String, String> map = new HashMap<>();
            map.put("error", error);
            return new ResponseEntity<Object>(map, HttpStatus.OK);
        }
    }

    @PutMapping("/artists/{id}")
    public ResponseEntity<Object> updateArtist(@RequestBody Artist artistDetails, @PathVariable(value = "id") long id) {
        Artist artist;
        Optional<Artist> opArt = repo.findById(id);
        if (opArt.isPresent()) {
            artist = opArt.get();
            artist.name = artistDetails.name;
            artist.age = artistDetails.age;
            repo.save(artist);
            return new ResponseEntity<Object>(artist, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "artist not found");
        }
    }

    @DeleteMapping("/artists/{id}")
    public ResponseEntity<Object> deleteArtist(@PathVariable(value = "id") Long artistId) {
        Optional<Artist> artist = repo.findById(artistId);
        Map<String, Boolean> resp = new HashMap<>();
        if (artist.isPresent()) {
            repo.delete(artist.get());
            resp.put("deleted", Boolean.TRUE);
        }
        else
            resp.put("deleted", Boolean.FALSE);
        return ResponseEntity.ok(resp);
    }
}
