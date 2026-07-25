package com.invisos.sims.communication.service;

import com.invisos.sims.communication.model.Announcements;
import com.invisos.sims.communication.repository.AnnouncementsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AnnouncementsServiceImpl implements AnnouncementsService {

    private final AnnouncementsRepository announcementsRepository;

    public AnnouncementsServiceImpl(AnnouncementsRepository announcementsRepository) {
        this.announcementsRepository = announcementsRepository;
    }

    @Override
    public List<Announcements> findAll() {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Announcements findById(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Announcements create(Announcements entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Announcements update(UUID id, Announcements entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void delete(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }
}
