package com.invisos.sims.academic.controller;

import com.invisos.sims.academic.model.Sections;
import com.invisos.sims.academic.service.SectionsService;
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
@RequestMapping("/api/v1/sections")
public class SectionsController {

    private final SectionsService sectionsService;

    public SectionsController(SectionsService sectionsService) {
        this.sectionsService = sectionsService;
    }

    @GetMapping
    public ResponseEntity<List<Sections>> getAll() {
        return ResponseEntity.ok(sectionsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sections> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(sectionsService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Sections> create(@RequestBody Sections entity) {
        return ResponseEntity.ok(sectionsService.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sections> update(@PathVariable UUID id, @RequestBody Sections entity) {
        return ResponseEntity.ok(sectionsService.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        sectionsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
