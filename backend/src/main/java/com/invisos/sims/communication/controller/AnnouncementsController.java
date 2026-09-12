package com.invisos.sims.communication.controller;

import com.invisos.sims.common.enums.AnnouncementPriority;
import com.invisos.sims.communication.dto.request.AnnouncementRequestDto;
import com.invisos.sims.communication.dto.response.AnnouncementResponseDto;
import com.invisos.sims.communication.mapper.AnnouncementMapper;
import com.invisos.sims.communication.model.Announcements;
import com.invisos.sims.communication.service.AnnouncementsService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Announcement feed for the announcements tab — visible to every user,
     * newest first, optionally filtered by priority.
     */
//    @PreAuthorize("isAuthenticated()") // TODO: any authenticated user
    @GetMapping
    public ResponseEntity<Page<AnnouncementResponseDto>> getAll(
            @RequestParam(required = false) AnnouncementPriority priority,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        return ResponseEntity.ok(
                announcementsService.findAll(priority, pageable).map(announcementMapper::toResponse));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: any authenticated user
    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(announcementMapper.toResponse(announcementsService.findById(id)));
    }

    /**
     * @deprecated use {@code GET /api/v1/announcements?priority=} instead.
     */
    @Deprecated
//    @PreAuthorize("isAuthenticated()") // TODO: any authenticated user
    @GetMapping("/priority/{priority}")
    public ResponseEntity<Page<AnnouncementResponseDto>> getByPriority(
            @PathVariable AnnouncementPriority priority,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        return ResponseEntity.ok(
                announcementsService.findAll(priority, pageable).map(announcementMapper::toResponse));
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
