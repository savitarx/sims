package com.invisos.sims.communication.service;

import com.invisos.sims.communication.model.Announcements;

import java.util.List;
import java.util.UUID;

public interface AnnouncementsService {

    List<Announcements> findAll();

    Announcements findById(UUID id);

    Announcements create(Announcements entity);

    Announcements update(UUID id, Announcements entity);

    void delete(UUID id);
}
