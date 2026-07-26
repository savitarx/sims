package com.invisos.sims.attendance.controller;

import com.invisos.sims.attendance.model.ClassTimetable;
import com.invisos.sims.attendance.service.ClassTimetableService;
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
@RequestMapping("/api/v1/class-timetables")
public class ClassTimetableController {

    private final ClassTimetableService classTimetableService;

    public ClassTimetableController(ClassTimetableService classTimetableService) {
        this.classTimetableService = classTimetableService;
    }

    @GetMapping
    public ResponseEntity<List<ClassTimetable>> getAll() {
        return ResponseEntity.ok(classTimetableService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassTimetable> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(classTimetableService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ClassTimetable> create(@RequestBody ClassTimetable entity) {
        return ResponseEntity.ok(classTimetableService.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassTimetable> update(@PathVariable UUID id, @RequestBody ClassTimetable entity) {
        return ResponseEntity.ok(classTimetableService.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        classTimetableService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
