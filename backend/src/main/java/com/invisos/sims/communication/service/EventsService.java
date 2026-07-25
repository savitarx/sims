package com.invisos.sims.communication.service;

import com.invisos.sims.communication.model.Events;

import java.util.List;
import java.util.UUID;

public interface EventsService {

    List<Events> findAll();

    Events findById(UUID id);

    Events create(Events entity);

    Events update(UUID id, Events entity);

    void delete(UUID id);
}
