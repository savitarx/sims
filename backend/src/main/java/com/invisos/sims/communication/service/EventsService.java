package com.invisos.sims.communication.service;

import com.invisos.sims.communication.dto.request.EventRequestDto;
import com.invisos.sims.communication.model.Events;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EventsService {

    Page<Events> findAll(Pageable pageable);

    Events findById(UUID id);

    /** Class-specific events only; does not include school-wide events. */
    Page<Events> findByClassId(UUID classId, Pageable pageable);

    /**
     * School calendar for a date range. When a class is given, its events are
     * returned together with the school-wide ones.
     */
    List<Events> findCalendar(LocalDate from, LocalDate to, UUID classId);

    /** Events still running or yet to start, soonest first. */
    Page<Events> findUpcoming(Pageable pageable);

    Events create(EventRequestDto request);

    Events update(UUID id, EventRequestDto request);

    void delete(UUID id);
}
