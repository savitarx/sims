package com.invisos.sims.communication.repository;

import com.invisos.sims.communication.model.Events;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventsRepository extends JpaRepository<Events, UUID> {

    @Override
    @EntityGraph(attributePaths = {"schoolClass", "createdBy", "updatedBy"})
    Page<Events> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"schoolClass", "createdBy", "updatedBy"})
    Optional<Events> findByEventId(UUID eventId);

    @EntityGraph(attributePaths = {"schoolClass", "createdBy", "updatedBy"})
    Page<Events> findBySchoolClassClassId(UUID classId, Pageable pageable);

    /**
     * School calendar for a date range. A null {@code classId} returns school-wide
     * events only; a non-null one returns that class's events <em>and</em> the
     * school-wide ones, which is what a class calendar actually needs to show.
     */
    @EntityGraph(attributePaths = {"schoolClass", "createdBy", "updatedBy"})
    @Query("""
            SELECT e FROM Events e
            WHERE e.startDate <= :to AND e.endDate >= :from
              AND (:classId IS NULL OR e.schoolClass IS NULL OR e.schoolClass.classId = :classId)
            ORDER BY e.startDate ASC
            """)
    List<Events> findCalendar(@Param("from") LocalDate from,
                              @Param("to") LocalDate to,
                              @Param("classId") UUID classId);

    /** Upcoming events (still running or yet to start), soonest first. */
    @EntityGraph(attributePaths = {"schoolClass", "createdBy", "updatedBy"})
    Page<Events> findByEndDateGreaterThanEqualOrderByStartDateAsc(LocalDate from, Pageable pageable);
}
