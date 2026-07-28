package com.invisos.sims.exam.controller;

import com.invisos.sims.exam.dto.request.ExamRequestDto;
import com.invisos.sims.exam.dto.response.ExamResponseDto;
import com.invisos.sims.exam.mapper.ExamMapper;
import com.invisos.sims.exam.model.Exams;
import com.invisos.sims.exam.service.ExamsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/exams")
public class ExamsController {

    private final ExamsService examsService;
    private final ExamMapper examMapper;

    public ExamsController(ExamsService examsService, ExamMapper examMapper) {
        this.examsService = examsService;
        this.examMapper = examMapper;
    }

    @GetMapping("/test")
    public String test() {
        return "Exam API Working!";
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER/STUDENT
    @GetMapping
    public ResponseEntity<List<ExamResponseDto>> getAll() {
        return ResponseEntity.ok(examMapper.toResponseList(examsService.findAll()));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER/STUDENT
    @GetMapping("/{id}")
    public ResponseEntity<ExamResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(examMapper.toResponse(examsService.findById(id)));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @PostMapping
    public ResponseEntity<ExamResponseDto> create(
            @Valid @RequestBody ExamRequestDto request) {

        Exams created = examsService.create(request);
        return ResponseEntity.ok(examMapper.toResponse(created));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @PutMapping("/{id}")
    public ResponseEntity<ExamResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody ExamRequestDto request) {

        Exams updated = examsService.update(id, request);
        return ResponseEntity.ok(examMapper.toResponse(updated));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        examsService.delete(id);
        return ResponseEntity.noContent().build();
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @PutMapping("/{id}/publish")
    public ResponseEntity<ExamResponseDto> publish(@PathVariable UUID id) {
        Exams publishedExam = examsService.publish(id);
        return ResponseEntity.ok(examMapper.toResponse(publishedExam));
    }
}