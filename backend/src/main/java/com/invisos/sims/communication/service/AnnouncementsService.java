package com.invisos.sims.communication.service;

import com.invisos.sims.common.enums.AnnouncementPriority;
import com.invisos.sims.communication.dto.request.AnnouncementRequestDto;
import com.invisos.sims.communication.model.Announcements;

import java.util.List;
import java.util.UUID;

public interface AnnouncementsService {

    List<Announcements> findAll();

    Announcements findById(UUID id);

    List<Announcements> findByPriority(AnnouncementPriority priority);

    Announcements create(AnnouncementRequestDto request);

    Announcements update(UUID id, AnnouncementRequestDto request);

    void delete(UUID id);
}
