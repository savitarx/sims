package com.invisos.sims.exam.controller;

import com.invisos.sims.exam.model.ExamSubjects;
import com.invisos.sims.exam.service.ExamSubjectsService;
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
@RequestMapping("/api/v1/exam-subjects")
public class ExamSubjectsController {

    private final ExamSubjectsService examSubjectsService;

    public ExamSubjectsController(ExamSubjectsService examSubjectsService) {
        this.examSubjectsService = examSubjectsService;
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @GetMapping
    public ResponseEntity<List<ExamSubjects>> getAll() {
        return ResponseEntity.ok(examSubjectsService.findAll());
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @GetMapping("/{id}")
    public ResponseEntity<ExamSubjects> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(examSubjectsService.findById(id));
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @PostMapping
    public ResponseEntity<ExamSubjects> create(@RequestBody ExamSubjects entity) {
        return ResponseEntity.ok(examSubjectsService.create(entity));
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @PutMapping("/{id}")
    public ResponseEntity<ExamSubjects> update(@PathVariable UUID id, @RequestBody ExamSubjects entity) {
        return ResponseEntity.ok(examSubjectsService.update(id, entity));
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        examSubjectsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
