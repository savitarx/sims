package com.invisos.sims.communication.service.impl;

import com.invisos.sims.admin.model.AdminStaff;
import com.invisos.sims.admin.repository.AdminStaffRepository;
import com.invisos.sims.common.enums.AdminDesignation;
import com.invisos.sims.common.enums.AnnouncementPriority;
import com.invisos.sims.common.enums.UserStatus;
import com.invisos.sims.common.exception.BusinessRuleViolationException;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import com.invisos.sims.communication.dto.request.AnnouncementRequestDto;
import com.invisos.sims.communication.mapper.AnnouncementMapper;
import com.invisos.sims.communication.model.Announcements;
import com.invisos.sims.communication.repository.AnnouncementsRepository;
import com.invisos.sims.communication.service.AnnouncementsService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class AnnouncementsServiceImpl implements AnnouncementsService {

    private final AnnouncementsRepository announcementsRepository;
    private final AdminStaffRepository adminStaffRepository;
    private final AnnouncementMapper announcementMapper;

    public AnnouncementsServiceImpl(AnnouncementsRepository announcementsRepository,
                                    AdminStaffRepository adminStaffRepository,
                                    AnnouncementMapper announcementMapper) {
        this.announcementsRepository = announcementsRepository;
        this.adminStaffRepository = adminStaffRepository;
        this.announcementMapper = announcementMapper;
    }

    @Override
    public Page<Announcements> findAll(AnnouncementPriority priority, Pageable pageable) {
        return priority == null
                ? announcementsRepository.findAll(pageable)
                : announcementsRepository.findByPriority(priority, pageable);
    }

    @Override
    public Announcements findById(UUID id) {
        return announcementsRepository.findByAnnouncementId(id).orElseThrow(() ->
                new ResourceNotFoundException("Announcement not found with id: " + id));
    }

    @Override
    public Announcements create(AnnouncementRequestDto dto) {

        AdminStaff actor = findPublisherOrThrow(dto.getActorId());

        Announcements announcement = announcementMapper.toEntity(dto);
        announcement.setCreatedBy(actor);
        announcement.setUpdatedBy(actor);
        if (announcement.getPriority() == null) {
            announcement.setPriority(AnnouncementPriority.NOTICE);
        }
        return announcementsRepository.save(announcement);
    }

    @Override
    public Announcements update(UUID id, AnnouncementRequestDto dto) {

        Announcements existingAnnouncement = findById(id);
        AdminStaff actor = findPublisherOrThrow(dto.getActorId());

        AnnouncementPriority currentPriority = existingAnnouncement.getPriority();
        announcementMapper.updateEntity(dto, existingAnnouncement);
        if (existingAnnouncement.getPriority() == null) {
            existingAnnouncement.setPriority(currentPriority);
        }
        existingAnnouncement.setUpdatedBy(actor);
        return announcementsRepository.save(existingAnnouncement);
    }

    @Override
    public void delete(UUID id) {
        Announcements announcement = findById(id);
        announcementsRepository.delete(announcement);
    }

    /**
     * Announcements are visible to every user, so only active admin staff acting as
     * ADMIN or PRINCIPAL may publish them.
     */
    private AdminStaff findPublisherOrThrow(UUID adminId) {

        AdminStaff actor = adminStaffRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Admin staff not found with id: " + adminId));

        if (actor.getStatus() != null && actor.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessRuleViolationException(
                    "Only active admin staff can publish announcements.");
        }
        if (actor.getDesignation() != AdminDesignation.ADMIN
                && actor.getDesignation() != AdminDesignation.PRINCIPAL) {
            throw new BusinessRuleViolationException(
                    "Only an ADMIN or PRINCIPAL can publish announcements.");
        }
        return actor;
    }
}
