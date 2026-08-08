package com.invisos.sims.admin.service;

import com.invisos.sims.admin.model.AdminStaff;
import com.invisos.sims.admin.repository.AdminStaffRepository;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AdminStaffServiceImpl implements AdminStaffService {

    private final AdminStaffRepository adminStaffRepository;

    public AdminStaffServiceImpl(AdminStaffRepository adminStaffRepository) {
        this.adminStaffRepository = adminStaffRepository;
    }

    @Override
    public List<AdminStaff> findAll() {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public AdminStaff findById(UUID id) {
        // TODO: implement
        return adminStaffRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Admin staff not found with id " + id));

    }

    @Override
    public AdminStaff create(AdminStaff entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public AdminStaff update(UUID id, AdminStaff entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void delete(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }
}
