package com.invisos.sims.student.controller;

import com.invisos.sims.student.model.StudentSubjects;
import com.invisos.sims.student.service.StudentSubjectsService;
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
@RequestMapping("/api/v1/student-subjects")
public class StudentSubjectsController {

    private final StudentSubjectsService studentSubjectsService;

    public StudentSubjectsController(StudentSubjectsService studentSubjectsService) {
        this.studentSubjectsService = studentSubjectsService;
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @GetMapping
    public ResponseEntity<List<StudentSubjects>> getAll() {
        return ResponseEntity.ok(studentSubjectsService.findAll());
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @GetMapping("/{id}")
    public ResponseEntity<StudentSubjects> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(studentSubjectsService.findById(id));
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @PostMapping
    public ResponseEntity<StudentSubjects> create(@RequestBody StudentSubjects entity) {
        return ResponseEntity.ok(studentSubjectsService.create(entity));
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @PutMapping("/{id}")
    public ResponseEntity<StudentSubjects> update(@PathVariable UUID id, @RequestBody StudentSubjects entity) {
        return ResponseEntity.ok(studentSubjectsService.update(id, entity));
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        studentSubjectsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
