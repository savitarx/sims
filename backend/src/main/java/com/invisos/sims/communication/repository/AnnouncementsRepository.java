package com.invisos.sims.communication.repository;

import com.invisos.sims.common.enums.AnnouncementPriority;
import com.invisos.sims.communication.model.Announcements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnnouncementsRepository extends JpaRepository<Announcements, UUID> {

    @Override
    @EntityGraph(attributePaths = {"createdBy", "updatedBy"})
    Page<Announcements> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"createdBy", "updatedBy"})
    Optional<Announcements> findByAnnouncementId(UUID announcementId);

    @EntityGraph(attributePaths = {"createdBy", "updatedBy"})
    Page<Announcements> findByPriority(AnnouncementPriority priority, Pageable pageable);
}
