package com.invisos.sims.communication.service.impl;

import com.invisos.sims.academic.model.Classes;
import com.invisos.sims.academic.repository.ClassesRepository;
import com.invisos.sims.admin.model.AdminStaff;
import com.invisos.sims.admin.repository.AdminStaffRepository;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import com.invisos.sims.communication.dto.request.EventRequestDto;
import com.invisos.sims.communication.mapper.EventMapper;
import com.invisos.sims.communication.model.Events;
import com.invisos.sims.communication.repository.EventsRepository;
import com.invisos.sims.communication.service.EventsService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class EventsServiceImpl implements EventsService {

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
    public List<Events> findAll() {
        return eventsRepository.findAll();
    }

    @Override
    public Events findById(UUID id) {
        return eventsRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Event not found with id: " + id));
    }

    @Override
    public List<Events> findByClassId(UUID classId) {
        return eventsRepository.findBySchoolClassClassId(classId);
    }

    @Override
    public Events create(EventRequestDto dto) {

        validateDateRange(dto);

        AdminStaff createdBy = findAdminOrThrow(dto.getCreatedById());
        Classes schoolClass = resolveClass(dto.getClassId());

        Events event = eventMapper.toEntity(dto);
        event.setCreatedBy(createdBy);
        event.setSchoolClass(schoolClass);
        return eventsRepository.save(event);
    }

    @Override
    public Events update(UUID id, EventRequestDto dto) {

        validateDateRange(dto);

        Events existingEvent = findById(id);
        AdminStaff createdBy = findAdminOrThrow(dto.getCreatedById());
        Classes schoolClass = resolveClass(dto.getClassId());

        eventMapper.updateEntity(dto, existingEvent);
        existingEvent.setCreatedBy(createdBy);
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
            throw new IllegalArgumentException("End date cannot be before the start date.");
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

    private AdminStaff findAdminOrThrow(UUID adminId) {
        return adminStaffRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin staff not found with id: " + adminId));
    }
}
