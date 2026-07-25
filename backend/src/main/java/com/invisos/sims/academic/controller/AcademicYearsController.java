package com.invisos.sims.academic.controller;

import com.invisos.sims.academic.model.AcademicYears;
import com.invisos.sims.academic.service.AcademicYearsService;
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
@RequestMapping("/api/v1/academic-years")
public class AcademicYearsController {

    private final AcademicYearsService academicYearsService;

    public AcademicYearsController(AcademicYearsService academicYearsService) {
        this.academicYearsService = academicYearsService;
    }

    @GetMapping
    public ResponseEntity<List<AcademicYears>> getAll() {
        return ResponseEntity.ok(academicYearsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcademicYears> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(academicYearsService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AcademicYears> create(@RequestBody AcademicYears entity) {
        return ResponseEntity.ok(academicYearsService.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcademicYears> update(@PathVariable UUID id, @RequestBody AcademicYears entity) {
        return ResponseEntity.ok(academicYearsService.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        academicYearsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
