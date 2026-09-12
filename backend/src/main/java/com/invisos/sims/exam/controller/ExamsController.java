package com.invisos.sims.exam.controller;

import com.invisos.sims.common.enums.ExamStatus;
import com.invisos.sims.exam.dto.request.ExamRequestDto;
import com.invisos.sims.exam.dto.response.ExamOverviewResponseDto;
import com.invisos.sims.exam.dto.response.ExamResponseDto;
import com.invisos.sims.exam.dto.response.PublishPreviewResponseDto;
import com.invisos.sims.exam.mapper.ExamMapper;
import com.invisos.sims.exam.model.Exams;
import com.invisos.sims.exam.service.ExamsService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<ExamResponseDto>> getAll(
            @RequestParam(required = false) UUID academicYearId,
            @RequestParam(required = false) ExamStatus status,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ExamResponseDto> page = examsService.findAll(academicYearId, status, pageable)
                .map(examMapper::toResponse);
        return ResponseEntity.ok(page);
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER/STUDENT
    @GetMapping("/{id}")
    public ResponseEntity<ExamResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(examMapper.toResponse(examsService.findById(id)));
    }

    /**
     * Exam dashboard: every subject with its schedule and marks-entry progress,
     * in one call instead of the several lookups this screen used to need.
     */
//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER
    @GetMapping("/{id}/overview")
    public ResponseEntity<ExamOverviewResponseDto> getOverview(@PathVariable UUID id) {
        return ResponseEntity.ok(examsService.getOverview(id));
    }

    /** Publish readiness checklist — what blocks publishing, and what to warn about. */
//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @GetMapping("/{id}/publish-preview")
    public ResponseEntity<PublishPreviewResponseDto> getPublishPreview(@PathVariable UUID id) {
        return ResponseEntity.ok(examsService.getPublishPreview(id));
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
