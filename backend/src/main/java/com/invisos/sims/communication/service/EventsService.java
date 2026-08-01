package com.invisos.sims.communication.service;

import com.invisos.sims.communication.dto.request.EventRequestDto;
import com.invisos.sims.communication.model.Events;

import java.util.List;
import java.util.UUID;

public interface EventsService {

    List<Events> findAll();

    Events findById(UUID id);

    List<Events> findByClassId(UUID classId);

    Events create(EventRequestDto request);

    Events update(UUID id, EventRequestDto request);

    void delete(UUID id);
}
