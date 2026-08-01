package com.invisos.sims.communication.repository;

import com.invisos.sims.communication.model.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventsRepository extends JpaRepository<Events, UUID> {

    Optional<Events> findByEventId(UUID eventId);

    List<Events> findBySchoolClassClassId(UUID classId);
}
