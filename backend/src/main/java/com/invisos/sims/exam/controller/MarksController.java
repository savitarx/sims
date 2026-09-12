package com.invisos.sims.exam.controller;

import com.invisos.sims.exam.dto.request.MarkRequestDto;
import com.invisos.sims.exam.dto.request.MarksSheetSaveRequestDto;
import com.invisos.sims.exam.dto.response.MarkResponseDto;
import com.invisos.sims.exam.dto.response.MarksSheetResponseDto;
import com.invisos.sims.exam.dto.response.StudentResultResponseDto;
import com.invisos.sims.exam.mapper.MarkMapper;
import com.invisos.sims.exam.model.Marks;
import com.invisos.sims.exam.service.MarksService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    // ------------------------------------------------------------------
    // Marks-entry workflow
    // ------------------------------------------------------------------

    /**
     * Marks-entry screen in one call: exam, subject, section and the full student
     * roster with any marks already recorded pre-filled.
     */
//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER
    @GetMapping("/sheet")
    public ResponseEntity<MarksSheetResponseDto> getMarksSheet(
            @RequestParam UUID examSubjectId,
            @RequestParam UUID sectionId) {

        return ResponseEntity.ok(marksService.getMarksSheet(examSubjectId, sectionId));
    }

    /** Transactional bulk save of a marks sheet; a null mark clears the cell. */
//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to TEACHER
    @PutMapping("/sheet")
    public ResponseEntity<MarksSheetResponseDto> saveMarksSheet(
            @Valid @RequestBody MarksSheetSaveRequestDto request) {

        return ResponseEntity.ok(marksService.saveMarksSheet(request));
    }

    /** One student's result across every subject of an exam. */
//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER/STUDENT
    @GetMapping("/results/student")
    public ResponseEntity<StudentResultResponseDto> getStudentResult(
            @RequestParam UUID examId,
            @RequestParam UUID enrollmentId) {

        return ResponseEntity.ok(marksService.getStudentResult(examId, enrollmentId));
    }

    /** Result summary for a whole section. */
//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER
    @GetMapping("/results/section")
    public ResponseEntity<List<StudentResultResponseDto>> getSectionResults(
            @RequestParam UUID examId,
            @RequestParam UUID sectionId) {

        return ResponseEntity.ok(marksService.getSectionResults(examId, sectionId));
    }

    // ------------------------------------------------------------------
    // CRUD (single-record correction)
    // ------------------------------------------------------------------

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER
    @GetMapping
    public ResponseEntity<Page<MarkResponseDto>> getAll(
            @PageableDefault(size = 20) Pageable pageable) {

        return ResponseEntity.ok(marksService.findAll(pageable).map(markMapper::toResponse));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER
    @GetMapping("/{id}")
    public ResponseEntity<MarkResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(markMapper.toResponse(marksService.findById(id)));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER
    @GetMapping("/exam-subject/{examSubjectId}")
    public ResponseEntity<Page<MarkResponseDto>> getByExamSubjectId(
            @PathVariable UUID examSubjectId,
            @PageableDefault(size = 50) Pageable pageable) {

        return ResponseEntity.ok(
                marksService.findByExamSubjectId(examSubjectId, pageable).map(markMapper::toResponse));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER/STUDENT
    @GetMapping("/enrollment/{enrollmentId}")
    public ResponseEntity<List<MarkResponseDto>> getByEnrollmentId(@PathVariable UUID enrollmentId) {
        return ResponseEntity.ok(markMapper.toResponseList(marksService.findByEnrollmentId(enrollmentId)));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to TEACHER
    @PostMapping
    public ResponseEntity<MarkResponseDto> create(@Valid @RequestBody MarkRequestDto request) {
        Marks created = marksService.create(request);
        return ResponseEntity.ok(markMapper.toResponse(created));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to TEACHER
    @PutMapping("/{id}")
    public ResponseEntity<MarkResponseDto> update(@PathVariable UUID id,
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
