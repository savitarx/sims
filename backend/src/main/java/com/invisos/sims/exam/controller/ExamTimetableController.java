package com.invisos.sims.exam.controller;

import com.invisos.sims.exam.dto.request.ExamTimetableRequestDto;
import com.invisos.sims.exam.dto.response.ExamTimetableResponseDto;
import com.invisos.sims.exam.mapper.ExamTimetableMapper;
import com.invisos.sims.exam.model.ExamTimetable;
import com.invisos.sims.exam.service.ExamTimetableService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/exam-timetables")
public class ExamTimetableController {

    private final ExamTimetableService examTimetableService;
    private final ExamTimetableMapper examTimetableMapper;

    public ExamTimetableController(ExamTimetableService examTimetableService,
                                   ExamTimetableMapper examTimetableMapper) {
        this.examTimetableService = examTimetableService;
        this.examTimetableMapper = examTimetableMapper;
    }

    @GetMapping("/test")
    public String test() {
        return "Exam Timetable API Working!";
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER/STUDENT
    @GetMapping
    public ResponseEntity<List<ExamTimetableResponseDto>> getAll() {
        return ResponseEntity.ok(examTimetableMapper.toResponseList(examTimetableService.findAll()));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER/STUDENT
    @GetMapping("/{id}")
    public ResponseEntity<ExamTimetableResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(examTimetableMapper.toResponse(examTimetableService.findById(id)));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER/STUDENT
    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<ExamTimetableResponseDto>> getByExamId(@PathVariable UUID examId) {
        return ResponseEntity.ok(examTimetableMapper.toResponseList(examTimetableService.findByExamId(examId)));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @PostMapping
    public ResponseEntity<ExamTimetableResponseDto> create(
            @Valid @RequestBody ExamTimetableRequestDto request) {

        ExamTimetable created = examTimetableService.create(request);
        return ResponseEntity.ok(examTimetableMapper.toResponse(created));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @PutMapping("/{id}")
    public ResponseEntity<ExamTimetableResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody ExamTimetableRequestDto request) {

        ExamTimetable updated = examTimetableService.update(id, request);
        return ResponseEntity.ok(examTimetableMapper.toResponse(updated));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        examTimetableService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
