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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/student-subjects")
public class StudentSubjectsController {

    private final StudentSubjectsService studentSubjectsService;

    public StudentSubjectsController(StudentSubjectsService studentSubjectsService) {
        this.studentSubjectsService = studentSubjectsService;
    }

    @GetMapping
    public ResponseEntity<List<StudentSubjects>> getAll() {
        return ResponseEntity.ok(studentSubjectsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentSubjects> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(studentSubjectsService.findById(id));
    }

    @PostMapping
    public ResponseEntity<StudentSubjects> create(@RequestBody StudentSubjects entity) {
        return ResponseEntity.ok(studentSubjectsService.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentSubjects> update(@PathVariable UUID id, @RequestBody StudentSubjects entity) {
        return ResponseEntity.ok(studentSubjectsService.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        studentSubjectsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
