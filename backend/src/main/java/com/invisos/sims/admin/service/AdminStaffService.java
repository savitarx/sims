package com.invisos.sims.admin.service;

import com.invisos.sims.admin.model.AdminStaff;

import java.util.List;
import java.util.UUID;

public interface AdminStaffService {

    List<AdminStaff> findAll();

    AdminStaff findById(UUID id);

    AdminStaff create(AdminStaff entity);

    AdminStaff update(UUID id, AdminStaff entity);

    void delete(UUID id);
}
