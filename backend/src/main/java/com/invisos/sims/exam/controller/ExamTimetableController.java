package com.invisos.sims.exam.controller;

import com.invisos.sims.exam.model.ExamTimetable;
import com.invisos.sims.exam.service.ExamTimetableService;
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
@RequestMapping("/api/v1/exam-timetables")
public class ExamTimetableController {

    private final ExamTimetableService examTimetableService;

    public ExamTimetableController(ExamTimetableService examTimetableService) {
        this.examTimetableService = examTimetableService;
    }

    @GetMapping
    public ResponseEntity<List<ExamTimetable>> getAll() {
        return ResponseEntity.ok(examTimetableService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamTimetable> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(examTimetableService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ExamTimetable> create(@RequestBody ExamTimetable entity) {
        return ResponseEntity.ok(examTimetableService.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamTimetable> update(@PathVariable UUID id, @RequestBody ExamTimetable entity) {
        return ResponseEntity.ok(examTimetableService.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        examTimetableService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
