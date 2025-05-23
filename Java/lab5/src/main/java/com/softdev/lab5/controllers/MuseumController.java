package com.softdev.lab5.controllers;

import com.softdev.lab5.models.Museum;
import com.softdev.lab5.models.User;
import com.softdev.lab5.repositories.MuseumRepo;
import com.softdev.lab5.repositories.UserRepo;
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
public class MuseumController {
    @Autowired
    MuseumRepo repo;

    @GetMapping("/museums")
    public Page<Museum> getAllMuseums(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return repo.findAll(PageRequest.of(page, limit).withSort(Sort.by(Sort.Direction.ASC, "name")));
    }

    @PostMapping("/museums")
    public ResponseEntity<Object> createMuseum(@RequestBody Museum museum) throws DataValidationException {
        try {
            return new ResponseEntity<>(repo.save(museum), HttpStatus.OK);
        }
        catch (Exception ex) {
            if (ex.getMessage().contains("uqname"))
                throw new DataValidationException("Этот музей уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }

    @PutMapping("/museums/{id}")
    public ResponseEntity<Museum> updateMuseum(@PathVariable(value = "id") Long museumId, @RequestBody Museum museumDetails) throws DataValidationException {
        Museum museum = repo.findById(museumId).orElseThrow(() -> new DataValidationException("Музей с таким индексом не найден"));
        museum.name = museumDetails.name;
        museum.location = museumDetails.location;
        return ResponseEntity.ok(repo.save(museum));
    }

    @PostMapping("/deleteMuseums")
    public ResponseEntity<List<Museum>> deleteMuseums(@RequestBody List<Museum> museums) {
        repo.deleteAll(museums);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("museums/{id}")
    public ResponseEntity<Object> deleteMuseum(@PathVariable(value = "id") Long museumId) {
        Optional<Museum> museum = repo.findById(museumId);
        Map<String, Boolean> resp = new HashMap<>();
        if (museum.isPresent()) {
            repo.delete(museum.get());
            resp.put("deleted", Boolean.TRUE);
        }
        else
            resp.put("deleted", Boolean.FALSE);
        return ResponseEntity.ok(resp);
    }
}
