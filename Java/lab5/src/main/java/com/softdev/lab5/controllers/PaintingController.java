package com.softdev.lab5.controllers;

import com.softdev.lab5.models.Artist;
import com.softdev.lab5.models.Country;
import com.softdev.lab5.models.Museum;
import com.softdev.lab5.models.Painting;
import com.softdev.lab5.repositories.ArtistRepo;
import com.softdev.lab5.repositories.CountryRepo;
import com.softdev.lab5.repositories.MuseumRepo;
import com.softdev.lab5.repositories.PaintingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class PaintingController {
    @Autowired
    PaintingRepo repo;
    @Autowired
    ArtistRepo artistRepo;
    @Autowired
    MuseumRepo museumRepo;

    @GetMapping("/paintings")
    public List<Painting> getAllPaintings() {
        return repo.findAll();
    }

    @PostMapping("/paintings")
    public ResponseEntity<Object> createPainting(@RequestBody Painting paint) throws Exception{
        try {
            Optional<Artist> optArt = artistRepo.findById(paint.artist.id);
            Optional<Museum> optMuseum = museumRepo.findById(paint.museum.id);
            if (optArt.isPresent()) {
                paint.artist = optArt.get();
            }
            if (optMuseum.isPresent()) {
                paint.museum = optMuseum.get();
            }
            return new ResponseEntity<>(repo.save(paint), HttpStatus.OK);
        }
        catch (Exception ex) {
            String error;
            if (ex.getMessage().contains("NOT NULL")) {
                error = "emptyPaintingNameError";
            }
            else {
                error = "undefinedError";
            }
            Map<String, String> map = new HashMap<>();
            map.put("error", error);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @PutMapping("/paintings/{id}")
    public ResponseEntity<Object> updatePainting(@RequestBody Painting paintingDetails, @PathVariable(value = "id") long id) {
        Painting painting;
        Optional<Painting> opPaint = repo.findById(id);
        if (opPaint.isPresent()) {
            painting = opPaint.get();
            painting.name = paintingDetails.name;
            painting.year = paintingDetails.year;
            repo.save(painting);
            return new ResponseEntity<>(painting, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "artist not found");
        }
    }

    @DeleteMapping("/paintings/{id}")
    public ResponseEntity<Object> deletePainting(@PathVariable(value = "id") Long paintingId) {
        Optional<Painting> painting = repo.findById(paintingId);
        Map<String, Boolean> resp = new HashMap<>();
        if (painting.isPresent()) {
            repo.delete(painting.get());
            resp.put("deleted", Boolean.TRUE);
        }
        else
            resp.put("deleted", Boolean.FALSE);
        return ResponseEntity.ok(resp);
    }
}
