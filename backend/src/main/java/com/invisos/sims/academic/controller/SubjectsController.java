package com.invisos.sims.academic.controller;

import com.invisos.sims.academic.dto.request.SubjectRequestDto;
import com.invisos.sims.academic.dto.response.SubjectResponseDto;
import com.invisos.sims.academic.mapper.SubjectMapper;
import com.invisos.sims.academic.model.Subjects;
import com.invisos.sims.academic.service.SubjectsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subjects")
public class SubjectsController {

    private final SubjectsService subjectsService;
    private final SubjectMapper subjectMapper;

    public SubjectsController(SubjectsService subjectsService, SubjectMapper subjectMapper) {
        this.subjectsService = subjectsService;
        this.subjectMapper = subjectMapper;
    }

    @GetMapping("/test")
    public String test() {
        return "Subject API Working!";
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER/STUDENT
    @GetMapping
    public ResponseEntity<List<SubjectResponseDto>> getAll() {
        return ResponseEntity.ok(subjectMapper.toResponseList(subjectsService.findAll()));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER/STUDENT
    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(subjectMapper.toResponse(subjectsService.findById(id)));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @PostMapping
    public ResponseEntity<SubjectResponseDto> create(
            @Valid @RequestBody SubjectRequestDto request) {

        Subjects created = subjectsService.create(request);
        return ResponseEntity.ok(subjectMapper.toResponse(created));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @PutMapping("/{id}")
    public ResponseEntity<SubjectResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody SubjectRequestDto request) {

        Subjects updated = subjectsService.update(id, request);
        return ResponseEntity.ok(subjectMapper.toResponse(updated));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        subjectsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
