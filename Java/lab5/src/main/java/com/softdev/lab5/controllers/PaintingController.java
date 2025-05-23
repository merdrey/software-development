package com.softdev.lab5.controllers;

import com.softdev.lab5.models.Painting;
import com.softdev.lab5.repositories.ArtistRepo;
import com.softdev.lab5.repositories.MuseumRepo;
import com.softdev.lab5.repositories.PaintingRepo;
import com.softdev.lab5.tools.DataValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/paintings")
    public Page<Painting> getAllPaintings(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return repo.findAll(PageRequest.of(page, limit).withSort(Sort.by(Sort.Direction.ASC, "name")));
    }

    @GetMapping("/paintings/{id}")
    public ResponseEntity<Painting> getPainting(@PathVariable ("id") long id) throws DataValidationException {
        Painting painting = repo.findById(id).orElseThrow(() -> new DataValidationException("Картина с таким индексом не найдена"));
        return new ResponseEntity<>(painting, HttpStatus.OK);
    }

    @PostMapping("/paintings")
    public ResponseEntity<Object> createPainting(@RequestBody Painting paint) throws DataValidationException{
        try {
            return new ResponseEntity<>(repo.save(paint), HttpStatus.CREATED);
        }
        catch (Exception ex) {
            if (ex.getMessage().contains("uqname"))
                throw new DataValidationException("Эта картина уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }

    @PutMapping("/paintings/{id}")
    public ResponseEntity<Object> updatePainting(@RequestBody Painting paintingDetails, @PathVariable(value = "id") long id) throws DataValidationException {
        try {
            Painting painting = repo.findById(id)
                    .orElseThrow(() -> new DataValidationException("Картина с таким индексом не найдена"));
            painting.name = paintingDetails.name;
            painting.year = paintingDetails.year;
            return new ResponseEntity<Object>(repo.save(painting), HttpStatus.OK);

        }
        catch (Exception ex) {
            if (ex.getMessage().contains("uqname"))
                throw new DataValidationException("Эта страна уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }

    @PostMapping("/deletePaintings")
    public ResponseEntity<List<Painting>> deletePaintings(@RequestBody List<Painting> paintings) {
        repo.deleteAll(paintings);
        return new ResponseEntity<>(HttpStatus.OK);
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
