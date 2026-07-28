package com.invisos.sims.exam.controller;

import com.invisos.sims.exam.dto.request.MarkRequestDto;
import com.invisos.sims.exam.dto.response.MarkResponseDto;
import com.invisos.sims.exam.mapper.MarkMapper;
import com.invisos.sims.exam.model.Marks;
import com.invisos.sims.exam.service.MarksService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/marks")
public class MarksController {

    private final MarksService marksService;
    private final MarkMapper markMapper;

    public MarksController(MarksService marksService, MarkMapper markMapper) {
        this.marksService = marksService;
        this.markMapper = markMapper;
    }

    @GetMapping("/test")
    public String test() {
        return "Marks API Working!";
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER
    @GetMapping
    public ResponseEntity<List<MarkResponseDto>> getAll() {
        return ResponseEntity.ok(markMapper.toResponseList(marksService.findAll()));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER/STUDENT
    @GetMapping("/{id}")
    public ResponseEntity<MarkResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(markMapper.toResponse(marksService.findById(id)));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER
    @GetMapping("/exam-subject/{examSubjectId}")
    public ResponseEntity<List<MarkResponseDto>> getByExamSubjectId(@PathVariable UUID examSubjectId) {
        return ResponseEntity.ok(markMapper.toResponseList(marksService.findByExamSubjectId(examSubjectId)));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER/STUDENT
    @GetMapping("/enrollment/{enrollmentId}")
    public ResponseEntity<List<MarkResponseDto>> getByEnrollmentId(@PathVariable UUID enrollmentId) {
        return ResponseEntity.ok(markMapper.toResponseList(marksService.findByEnrollmentId(enrollmentId)));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to TEACHER (entering marks)
    @PostMapping
    public ResponseEntity<MarkResponseDto> create(
            @Valid @RequestBody MarkRequestDto request) {

        Marks created = marksService.create(request);
        return ResponseEntity.ok(markMapper.toResponse(created));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to TEACHER (editing marks)
    @PutMapping("/{id}")
    public ResponseEntity<MarkResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody MarkRequestDto request) {

        Marks updated = marksService.update(id, request);
        return ResponseEntity.ok(markMapper.toResponse(updated));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        marksService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
