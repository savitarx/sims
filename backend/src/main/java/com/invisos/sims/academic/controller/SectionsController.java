package com.invisos.sims.academic.controller;

import com.invisos.sims.academic.dto.request.SectionRequestDto;
import com.invisos.sims.academic.dto.response.SectionResponseDto;
import com.invisos.sims.academic.model.Sections;
import com.invisos.sims.academic.service.SectionsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sections")
public class SectionsController {

    private final SectionsService sectionsService;

    public SectionsController(SectionsService sectionsService) {
        this.sectionsService = sectionsService;
    }

//    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @GetMapping
    public ResponseEntity<List<SectionResponseDto>> getAll(@RequestParam UUID academicYearId,@RequestParam(required = false) UUID classId) {
        return ResponseEntity.status(HttpStatus.OK).body(sectionsService.findAll(academicYearId,classId));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @GetMapping("/{id}")
    public ResponseEntity<SectionResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(sectionsService.findById(id));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @PostMapping
    public ResponseEntity<SectionResponseDto> create(@RequestBody SectionRequestDto entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sectionsService.create(entity));
    }

    @PatchMapping("/{sectionId}/class-teacher/{teacherId}")
    public ResponseEntity<SectionResponseDto> assignClassTeacher(@PathVariable UUID sectionId, @PathVariable UUID teacherId){
        return ResponseEntity.status(HttpStatus.OK).body(sectionsService.assignClassTeacher(sectionId,teacherId));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @PutMapping("/{id}")
    public ResponseEntity<SectionResponseDto> update(@PathVariable UUID id, @RequestBody SectionRequestDto entity) {
        return ResponseEntity.ok(sectionsService.update(id, entity));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        sectionsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
