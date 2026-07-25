package com.invisos.sims.teacher.controller;

import com.invisos.sims.teacher.model.Teachers;
import com.invisos.sims.teacher.service.TeachersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teachers")
public class TeachersController {

    private final TeachersService teachersService;

    public TeachersController(TeachersService teachersService) {
        this.teachersService = teachersService;
    }

    @GetMapping
    public ResponseEntity<List<Teachers>> getAll() {
        return ResponseEntity.ok(teachersService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teachers> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(teachersService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Teachers> create(@RequestBody Teachers entity) {
        return ResponseEntity.ok(teachersService.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Teachers> update(@PathVariable UUID id, @RequestBody Teachers entity) {
        return ResponseEntity.ok(teachersService.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        teachersService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
