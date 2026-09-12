package com.invisos.sims.communication.service.impl;

import com.invisos.sims.academic.model.Classes;
import com.invisos.sims.academic.repository.ClassesRepository;
import com.invisos.sims.admin.model.AdminStaff;
import com.invisos.sims.admin.repository.AdminStaffRepository;
import com.invisos.sims.common.enums.AdminDesignation;
import com.invisos.sims.common.enums.UserStatus;
import com.invisos.sims.common.exception.BusinessRuleViolationException;
import com.invisos.sims.common.exception.InvalidRequestException;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import com.invisos.sims.communication.dto.request.EventRequestDto;
import com.invisos.sims.communication.mapper.EventMapper;
import com.invisos.sims.communication.model.Events;
import com.invisos.sims.communication.repository.EventsRepository;
import com.invisos.sims.communication.service.EventsService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class EventsServiceImpl implements EventsService {

    /** Guard against unbounded calendar scans. */
    private static final int MAX_CALENDAR_DAYS = 400;

    private final EventsRepository eventsRepository;
    private final ClassesRepository classesRepository;
    private final AdminStaffRepository adminStaffRepository;
    private final EventMapper eventMapper;

    public EventsServiceImpl(EventsRepository eventsRepository,
                             ClassesRepository classesRepository,
                             AdminStaffRepository adminStaffRepository,
                             EventMapper eventMapper) {
        this.eventsRepository = eventsRepository;
        this.classesRepository = classesRepository;
        this.adminStaffRepository = adminStaffRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public Page<Events> findAll(Pageable pageable) {
        return eventsRepository.findAll(pageable);
    }

    @Override
    public Events findById(UUID id) {
        return eventsRepository.findByEventId(id).orElseThrow(() ->
                new ResourceNotFoundException("Event not found with id: " + id));
    }

    @Override
    public Page<Events> findByClassId(UUID classId, Pageable pageable) {
        return eventsRepository.findBySchoolClassClassId(classId, pageable);
    }

    @Override
    public List<Events> findCalendar(LocalDate from, LocalDate to, UUID classId) {

        if (from.isAfter(to)) {
            throw new InvalidRequestException("'from' date cannot be after the 'to' date.");
        }
        if (from.plusDays(MAX_CALENDAR_DAYS).isBefore(to)) {
            throw new InvalidRequestException(
                    "Calendar range cannot exceed " + MAX_CALENDAR_DAYS + " days.");
        }
        return eventsRepository.findCalendar(from, to, classId);
    }

    @Override
    public Page<Events> findUpcoming(Pageable pageable) {
        return eventsRepository.findByEndDateGreaterThanEqualOrderByStartDateAsc(LocalDate.now(), pageable);
    }

    @Override
    public Events create(EventRequestDto dto) {

        validateDateRange(dto);
        AdminStaff actor = findOrganiserOrThrow(dto.getActorId());
        Classes schoolClass = resolveClass(dto.getClassId());

        Events event = eventMapper.toEntity(dto);
        event.setCreatedBy(actor);
        event.setUpdatedBy(actor);
        event.setSchoolClass(schoolClass);
        return eventsRepository.save(event);
    }

    @Override
    public Events update(UUID id, EventRequestDto dto) {

        validateDateRange(dto);
        Events existingEvent = findById(id);
        AdminStaff actor = findOrganiserOrThrow(dto.getActorId());
        Classes schoolClass = resolveClass(dto.getClassId());

        eventMapper.updateEntity(dto, existingEvent);
        existingEvent.setUpdatedBy(actor);
        existingEvent.setSchoolClass(schoolClass);
        return eventsRepository.save(existingEvent);
    }

    @Override
    public void delete(UUID id) {
        Events event = findById(id);
        eventsRepository.delete(event);
    }

    private void validateDateRange(EventRequestDto dto) {
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new InvalidRequestException("End date cannot be before the start date.");
        }
    }

    /** A null class id is allowed and marks the event as school-wide. */
    private Classes resolveClass(UUID classId) {
        if (classId == null) {
            return null;
        }
        return classesRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + classId));
    }

    private AdminStaff findOrganiserOrThrow(UUID adminId) {

        AdminStaff actor = adminStaffRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Admin staff not found with id: " + adminId));

        if (actor.getStatus() != null && actor.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessRuleViolationException("Only active admin staff can manage events.");
        }
        if (actor.getDesignation() != AdminDesignation.ADMIN
                && actor.getDesignation() != AdminDesignation.PRINCIPAL) {
            throw new BusinessRuleViolationException("Only an ADMIN or PRINCIPAL can manage events.");
        }
        return actor;
    }
}
