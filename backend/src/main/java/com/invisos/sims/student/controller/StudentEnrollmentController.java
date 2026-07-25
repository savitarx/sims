package com.invisos.sims.student.controller;

import com.invisos.sims.student.model.StudentEnrollment;
import com.invisos.sims.student.service.StudentEnrollmentService;
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
@RequestMapping("/api/v1/student-enrollments")
public class StudentEnrollmentController {

    private final StudentEnrollmentService studentEnrollmentService;

    public StudentEnrollmentController(StudentEnrollmentService studentEnrollmentService) {
        this.studentEnrollmentService = studentEnrollmentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentEnrollment>> getAll() {
        return ResponseEntity.ok(studentEnrollmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentEnrollment> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(studentEnrollmentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<StudentEnrollment> create(@RequestBody StudentEnrollment entity) {
        return ResponseEntity.ok(studentEnrollmentService.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentEnrollment> update(@PathVariable UUID id, @RequestBody StudentEnrollment entity) {
        return ResponseEntity.ok(studentEnrollmentService.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        studentEnrollmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
