package com.invisos.sims.exam.controller;

import com.invisos.sims.exam.model.Exams;
import com.invisos.sims.exam.service.ExamsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/exams")
public class ExamsController {

    private final ExamsService examsService;

    public ExamsController(ExamsService examsService) {
        this.examsService = examsService;
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @GetMapping
    public ResponseEntity<List<Exams>> getAll() {
        return ResponseEntity.ok(examsService.findAll());
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @GetMapping("/{id}")
    public ResponseEntity<Exams> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(examsService.findById(id));
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @PostMapping
    public ResponseEntity<Exams> create(@RequestBody Exams entity) {
        return ResponseEntity.ok(examsService.create(entity));
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @PutMapping("/{id}")
    public ResponseEntity<Exams> update(@PathVariable UUID id, @RequestBody Exams entity) {
        return ResponseEntity.ok(examsService.update(id, entity));
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        examsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
