package com.invisos.sims.academic.controller;

import com.invisos.sims.academic.model.Classes;
import com.invisos.sims.academic.service.ClassesService;
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
@RequestMapping("/api/v1/classes")
public class ClassesController {

    private final ClassesService classesService;

    public ClassesController(ClassesService classesService) {
        this.classesService = classesService;
    }

    @GetMapping
    public ResponseEntity<List<Classes>> getAll() {
        return ResponseEntity.ok(classesService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Classes> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(classesService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Classes> create(@RequestBody Classes entity) {
        return ResponseEntity.ok(classesService.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Classes> update(@PathVariable UUID id, @RequestBody Classes entity) {
        return ResponseEntity.ok(classesService.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        classesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
