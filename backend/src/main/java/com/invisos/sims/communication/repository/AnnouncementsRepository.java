package com.invisos.sims.communication.repository;

import com.invisos.sims.common.enums.AnnouncementPriority;
import com.invisos.sims.communication.model.Announcements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnnouncementsRepository extends JpaRepository<Announcements, UUID> {

    Optional<Announcements> findByAnnouncementId(UUID announcementId);

    List<Announcements> findByPriority(AnnouncementPriority priority);
}
