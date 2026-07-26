package com.invisos.sims.student.controller;

import com.invisos.sims.student.model.Students;
import com.invisos.sims.student.service.StudentsService;
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
@RequestMapping("/api/v1/students")
public class StudentsController {

    private final StudentsService studentsService;
    
    @GetMapping("/test")
    public String test() {
    	return "Test Api!!";
    }

    public StudentsController(StudentsService studentsService) {
        this.studentsService = studentsService;
    }

    @GetMapping
    public ResponseEntity<List<Students>> getAll() {
        return ResponseEntity.ok(studentsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Students> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(studentsService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Students> create(@RequestBody Students entity) {
        return ResponseEntity.ok(studentsService.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Students> update(@PathVariable UUID id, @RequestBody Students entity) {
        return ResponseEntity.ok(studentsService.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        studentsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
