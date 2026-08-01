package com.invisos.sims.communication.controller;

import com.invisos.sims.common.enums.AnnouncementPriority;
import com.invisos.sims.communication.dto.request.AnnouncementRequestDto;
import com.invisos.sims.communication.dto.response.AnnouncementResponseDto;
import com.invisos.sims.communication.mapper.AnnouncementMapper;
import com.invisos.sims.communication.model.Announcements;
import com.invisos.sims.communication.service.AnnouncementsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/announcements")
public class AnnouncementsController {

    private final AnnouncementsService announcementsService;
    private final AnnouncementMapper announcementMapper;

    public AnnouncementsController(AnnouncementsService announcementsService,
                                   AnnouncementMapper announcementMapper) {
        this.announcementsService = announcementsService;
        this.announcementMapper = announcementMapper;
    }

    @GetMapping("/test")
    public String test() {
        return "Announcement API Working!";
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER/STUDENT
    @GetMapping
    public ResponseEntity<List<AnnouncementResponseDto>> getAll() {
        return ResponseEntity.ok(announcementMapper.toResponseList(announcementsService.findAll()));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER/STUDENT
    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(announcementMapper.toResponse(announcementsService.findById(id)));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER/STUDENT
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<AnnouncementResponseDto>> getByPriority(
            @PathVariable AnnouncementPriority priority) {

        return ResponseEntity.ok(
                announcementMapper.toResponseList(announcementsService.findByPriority(priority)));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @PostMapping
    public ResponseEntity<AnnouncementResponseDto> create(
            @Valid @RequestBody AnnouncementRequestDto request) {

        Announcements created = announcementsService.create(request);
        return ResponseEntity.ok(announcementMapper.toResponse(created));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @PutMapping("/{id}")
    public ResponseEntity<AnnouncementResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody AnnouncementRequestDto request) {

        Announcements updated = announcementsService.update(id, request);
        return ResponseEntity.ok(announcementMapper.toResponse(updated));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        announcementsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
