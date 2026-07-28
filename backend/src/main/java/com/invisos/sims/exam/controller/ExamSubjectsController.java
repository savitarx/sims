package com.invisos.sims.exam.controller;

import com.invisos.sims.exam.dto.request.ExamSubjectRequestDto;
import com.invisos.sims.exam.dto.response.ExamSubjectResponseDto;
import com.invisos.sims.exam.mapper.ExamSubjectMapper;
import com.invisos.sims.exam.model.ExamSubjects;
import com.invisos.sims.exam.service.ExamSubjectsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/exam-subjects")
public class ExamSubjectsController {

    private final ExamSubjectsService examSubjectsService;
    private final ExamSubjectMapper examSubjectMapper;

    public ExamSubjectsController(ExamSubjectsService examSubjectsService,
                                  ExamSubjectMapper examSubjectMapper) {
        this.examSubjectsService = examSubjectsService;
        this.examSubjectMapper = examSubjectMapper;
    }

    @GetMapping("/test")
    public String test() {
        return "Exam Subject API Working!";
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER/STUDENT
    @GetMapping
    public ResponseEntity<List<ExamSubjectResponseDto>> getAll() {
        return ResponseEntity.ok(examSubjectMapper.toResponseList(examSubjectsService.findAll()));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER/STUDENT
    @GetMapping("/{id}")
    public ResponseEntity<ExamSubjectResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(examSubjectMapper.toResponse(examSubjectsService.findById(id)));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER/STUDENT
    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<ExamSubjectResponseDto>> getByExamId(@PathVariable UUID examId) {
        return ResponseEntity.ok(examSubjectMapper.toResponseList(examSubjectsService.findByExamId(examId)));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @PostMapping
    public ResponseEntity<ExamSubjectResponseDto> create(
            @Valid @RequestBody ExamSubjectRequestDto request) {

        ExamSubjects created = examSubjectsService.create(request);
        return ResponseEntity.ok(examSubjectMapper.toResponse(created));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @PutMapping("/{id}")
    public ResponseEntity<ExamSubjectResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody ExamSubjectRequestDto request) {

        ExamSubjects updated = examSubjectsService.update(id, request);
        return ResponseEntity.ok(examSubjectMapper.toResponse(updated));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        examSubjectsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
