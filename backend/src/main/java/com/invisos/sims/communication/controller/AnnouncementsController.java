package com.invisos.sims.communication.controller;

import com.invisos.sims.communication.model.Announcements;
import com.invisos.sims.communication.service.AnnouncementsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/announcements")
public class AnnouncementsController {

    private final AnnouncementsService announcementsService;

    public AnnouncementsController(AnnouncementsService announcementsService) {
        this.announcementsService = announcementsService;
    }

    @GetMapping
    public ResponseEntity<List<Announcements>> getAll() {
        return ResponseEntity.ok(announcementsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Announcements> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(announcementsService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Announcements> create(@RequestBody Announcements entity) {
        return ResponseEntity.ok(announcementsService.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Announcements> update(@PathVariable UUID id, @RequestBody Announcements entity) {
        return ResponseEntity.ok(announcementsService.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        announcementsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
