package com.softdev.lab5.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.softdev.lab5.models.Museum;
import com.softdev.lab5.models.Painting;
import com.softdev.lab5.models.User;
import com.softdev.lab5.repositories.MuseumRepo;
import com.softdev.lab5.repositories.UserRepo;
import com.softdev.lab5.tools.DataValidationException;
import com.softdev.lab5.tools.Utils;
import com.softdev.lab5.tools.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    UserRepo repo;
    @Autowired
    MuseumRepo museumRepo;

    @GetMapping("/users")
    public Page<User> getAllUsers(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return repo.findAll(PageRequest.of(page, limit));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") long id) throws DataValidationException {
        User user = repo.findById(id).orElseThrow(() -> new DataValidationException("Пользователь с таким индексом не найден"));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@RequestBody User user) throws DataValidationException {
        try {
            return new ResponseEntity<>(repo.save(user), HttpStatus.CREATED);
        }
        catch (Exception ex) {
            if (ex.getMessage().contains("uqlogin"))
                throw new DataValidationException("Этот пользователь уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }

    @PostMapping("/users/{id}/addmuseums")
    public ResponseEntity<Object> addMuseums(@PathVariable(value = "id") Long userId, @RequestBody Set<Museum> museums) {
        Optional<User> uu = repo.findById(userId);
        int cnt = 0;
        if (uu.isPresent()) {
            User u = uu.get();
            for (Museum m : museums) {
                Optional<Museum> mm = museumRepo.findById(m.id);
                if (mm.isPresent()) {
                    u.addMuseum(mm.get());
                    cnt++;
                }
            }
            repo.save(u);
        }
        Map<String, String> response = new HashMap<>();
        response.put("count", String.valueOf(cnt));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity updateUser(@PathVariable(value = "id") Long userId,
                                     @RequestBody User userDetails)
            throws DataValidationException
    {
        try {
            User user = repo.findById(userId)
                    .orElseThrow(() -> new DataValidationException(" Пользователь с таким индексом не найден"));
            user.email = userDetails.email;
            String np = userDetails.np;
            if (np != null  && !np.isEmpty()) {
                byte[] b = new byte[32];
                new Random().nextBytes(b);
                String salt = new String(Hex.encode(b));
                user.password = Utils.ComputeHash(np, salt);
                user.salt = salt;
            }
            repo.save(user);
            return ResponseEntity.ok(user);
        }
        catch (Exception ex) {
            if (ex.getMessage().contains("emailuqname"))
                throw new DataValidationException("Пользователь с такой почтой уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }

    @PostMapping("/users/{id}/removemuseums")
    public ResponseEntity<Object> removeMuseums(@PathVariable(value = "id") Long userId, @RequestBody Set<Museum> museums) {
        Optional<User> uu = repo.findById(userId);
        int cnt = 0;
        if (uu.isPresent()) {
            User u = uu.get();
            for (Museum m : u.museums) {
                u.removeMuseum(m);
                cnt++;
            }
            repo.save(u);
        }
        Map<String, String> response = new HashMap<>();
        response.put("count", String.valueOf(cnt));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deleteUsers")
    public ResponseEntity<List<User>> deleteUsers(@RequestBody List<User> users) {
        repo.deleteAll(users);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") Long userId) {
        Optional<User> user = repo.findById(userId);
        Map<String, Boolean> resp = new HashMap<>();
        if (user.isPresent()) {
            repo.delete(user.get());
            resp.put("deleted", Boolean.TRUE);
        }
        else
            resp.put("deleted", Boolean.FALSE);
        return ResponseEntity.ok(resp);
    }
}
