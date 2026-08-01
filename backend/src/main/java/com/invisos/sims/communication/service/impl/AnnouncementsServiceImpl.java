package com.invisos.sims.communication.service.impl;

import com.invisos.sims.admin.model.AdminStaff;
import com.invisos.sims.admin.repository.AdminStaffRepository;
import com.invisos.sims.common.enums.AnnouncementPriority;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import com.invisos.sims.communication.dto.request.AnnouncementRequestDto;
import com.invisos.sims.communication.mapper.AnnouncementMapper;
import com.invisos.sims.communication.model.Announcements;
import com.invisos.sims.communication.repository.AnnouncementsRepository;
import com.invisos.sims.communication.service.AnnouncementsService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public List<Announcements> findAll() {
        return announcementsRepository.findAll();
    }

    @Override
    public Announcements findById(UUID id) {
        return announcementsRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Announcement not found with id: " + id));
    }

    @Override
    public List<Announcements> findByPriority(AnnouncementPriority priority) {
        return announcementsRepository.findByPriority(priority);
    }

    @Override
    public Announcements create(AnnouncementRequestDto dto) {

        AdminStaff createdBy = findAdminOrThrow(dto.getCreatedById());

        Announcements announcement = announcementMapper.toEntity(dto);
        announcement.setCreatedBy(createdBy);
        if (announcement.getPriority() == null) {
            announcement.setPriority(AnnouncementPriority.NOTICE);
        }
        return announcementsRepository.save(announcement);
    }

    @Override
    public Announcements update(UUID id, AnnouncementRequestDto dto) {

        Announcements existingAnnouncement = findById(id);
        AdminStaff createdBy = findAdminOrThrow(dto.getCreatedById());

        announcementMapper.updateEntity(dto, existingAnnouncement);
        existingAnnouncement.setCreatedBy(createdBy);
        return announcementsRepository.save(existingAnnouncement);
    }

    @Override
    public void delete(UUID id) {
        Announcements announcement = findById(id);
        announcementsRepository.delete(announcement);
    }

    private AdminStaff findAdminOrThrow(UUID adminId) {
        return adminStaffRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin staff not found with id: " + adminId));
    }
}
