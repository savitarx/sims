package com.invisos.sims.teacher.controller;

import com.invisos.sims.teacher.model.TeacherAssignment;
import com.invisos.sims.teacher.service.TeacherAssignmentService;
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
@RequestMapping("/api/v1/teacher-assignments")
public class TeacherAssignmentController {

    private final TeacherAssignmentService teacherAssignmentService;

    public TeacherAssignmentController(TeacherAssignmentService teacherAssignmentService) {
        this.teacherAssignmentService = teacherAssignmentService;
    }

    @GetMapping
    public ResponseEntity<List<TeacherAssignment>> getAll() {
        return ResponseEntity.ok(teacherAssignmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherAssignment> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(teacherAssignmentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<TeacherAssignment> create(@RequestBody TeacherAssignment entity) {
        return ResponseEntity.ok(teacherAssignmentService.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherAssignment> update(@PathVariable UUID id, @RequestBody TeacherAssignment entity) {
        return ResponseEntity.ok(teacherAssignmentService.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        teacherAssignmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
