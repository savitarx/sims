package com.invisos.sims.communication.repository;

import com.invisos.sims.communication.model.Announcements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AnnouncementsRepository extends JpaRepository<Announcements, UUID> {
}
