package com.invisos.sims.student.controller;

import com.invisos.sims.student.model.Parents;
import com.invisos.sims.student.service.ParentsService;
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
@RequestMapping("/api/v1/parents")
public class ParentsController {

    private final ParentsService parentsService;

    public ParentsController(ParentsService parentsService) {
        this.parentsService = parentsService;
    }

    @GetMapping
    public ResponseEntity<List<Parents>> getAll() {
        return ResponseEntity.ok(parentsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parents> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(parentsService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Parents> create(@RequestBody Parents entity) {
        return ResponseEntity.ok(parentsService.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Parents> update(@PathVariable UUID id, @RequestBody Parents entity) {
        return ResponseEntity.ok(parentsService.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        parentsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
