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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/exams")
public class ExamsController {

    private final ExamsService examsService;

    public ExamsController(ExamsService examsService) {
        this.examsService = examsService;
    }

    @GetMapping
    public ResponseEntity<List<Exams>> getAll() {
        return ResponseEntity.ok(examsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exams> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(examsService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Exams> create(@RequestBody Exams entity) {
        return ResponseEntity.ok(examsService.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exams> update(@PathVariable UUID id, @RequestBody Exams entity) {
        return ResponseEntity.ok(examsService.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        examsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
