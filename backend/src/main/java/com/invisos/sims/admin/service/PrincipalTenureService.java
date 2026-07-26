package com.invisos.sims.admin.service;

import com.invisos.sims.admin.model.PrincipalTenure;

import java.util.List;
import java.util.UUID;

public interface PrincipalTenureService {

    List<PrincipalTenure> findAll();

    PrincipalTenure findById(UUID id);

    PrincipalTenure create(PrincipalTenure entity);

    PrincipalTenure update(UUID id, PrincipalTenure entity);

    void delete(UUID id);
}
