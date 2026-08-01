package com.invisos.sims.communication.controller;

import com.invisos.sims.communication.dto.request.EventRequestDto;
import com.invisos.sims.communication.dto.response.EventResponseDto;
import com.invisos.sims.communication.mapper.EventMapper;
import com.invisos.sims.communication.model.Events;
import com.invisos.sims.communication.service.EventsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
public class EventsController {

    private final EventsService eventsService;
    private final EventMapper eventMapper;

    public EventsController(EventsService eventsService, EventMapper eventMapper) {
        this.eventsService = eventsService;
        this.eventMapper = eventMapper;
    }

    @GetMapping("/test")
    public String test() {
        return "Event API Working!";
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER/STUDENT
    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getAll() {
        return ResponseEntity.ok(eventMapper.toResponseList(eventsService.findAll()));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER/STUDENT
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(eventMapper.toResponse(eventsService.findById(id)));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER/STUDENT
    @GetMapping("/class/{classId}")
    public ResponseEntity<List<EventResponseDto>> getByClassId(@PathVariable UUID classId) {
        return ResponseEntity.ok(eventMapper.toResponseList(eventsService.findByClassId(classId)));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @PostMapping
    public ResponseEntity<EventResponseDto> create(
            @Valid @RequestBody EventRequestDto request) {

        Events created = eventsService.create(request);
        return ResponseEntity.ok(eventMapper.toResponse(created));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody EventRequestDto request) {

        Events updated = eventsService.update(id, request);
        return ResponseEntity.ok(eventMapper.toResponse(updated));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        eventsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
