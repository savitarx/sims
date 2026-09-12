package com.invisos.sims.communication.controller;

import com.invisos.sims.communication.dto.request.EventRequestDto;
import com.invisos.sims.communication.dto.response.EventResponseDto;
import com.invisos.sims.communication.mapper.EventMapper;
import com.invisos.sims.communication.model.Events;
import com.invisos.sims.communication.service.EventsService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    /**
     * School calendar for a date range. Passing a class returns that class's events
     * together with the school-wide ones — what a calendar screen actually shows.
     */
//    @PreAuthorize("isAuthenticated()") // TODO: any authenticated user
    @GetMapping("/calendar")
    public ResponseEntity<List<EventResponseDto>> getCalendar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) UUID classId) {

        return ResponseEntity.ok(
                eventMapper.toResponseList(eventsService.findCalendar(from, to, classId)));
    }

    /** Dashboard widget: events still running or yet to start, soonest first. */
//    @PreAuthorize("isAuthenticated()") // TODO: any authenticated user
    @GetMapping("/upcoming")
    public ResponseEntity<Page<EventResponseDto>> getUpcoming(
            @PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(eventsService.findUpcoming(pageable).map(eventMapper::toResponse));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: any authenticated user
    @GetMapping
    public ResponseEntity<Page<EventResponseDto>> getAll(
            @PageableDefault(size = 20, sort = "startDate") Pageable pageable) {

        return ResponseEntity.ok(eventsService.findAll(pageable).map(eventMapper::toResponse));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: any authenticated user
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(eventMapper.toResponse(eventsService.findById(id)));
    }

    /**
     * Class-specific events only. School-wide events are excluded — use
     * {@code /calendar?classId=} for the combined view.
     */
//    @PreAuthorize("isAuthenticated()") // TODO: any authenticated user
    @GetMapping("/class/{classId}")
    public ResponseEntity<Page<EventResponseDto>> getByClassId(
            @PathVariable UUID classId,
            @PageableDefault(size = 20, sort = "startDate") Pageable pageable) {

        return ResponseEntity.ok(
                eventsService.findByClassId(classId, pageable).map(eventMapper::toResponse));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @PostMapping
    public ResponseEntity<EventResponseDto> create(@Valid @RequestBody EventRequestDto request) {
        Events created = eventsService.create(request);
        return ResponseEntity.ok(eventMapper.toResponse(created));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDto> update(@PathVariable UUID id,
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
