package com.invisos.sims.communication.service;

import com.invisos.sims.common.enums.AnnouncementPriority;
import com.invisos.sims.communication.dto.request.AnnouncementRequestDto;
import com.invisos.sims.communication.model.Announcements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AnnouncementsService {

    /** Announcement feed, optionally filtered by priority. */
    Page<Announcements> findAll(AnnouncementPriority priority, Pageable pageable);

    Announcements findById(UUID id);

    Announcements create(AnnouncementRequestDto request);

    Announcements update(UUID id, AnnouncementRequestDto request);

    void delete(UUID id);
}
