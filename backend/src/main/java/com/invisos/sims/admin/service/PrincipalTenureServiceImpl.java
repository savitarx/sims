package com.invisos.sims.admin.service;

import com.invisos.sims.admin.model.PrincipalTenure;
import com.invisos.sims.admin.repository.PrincipalTenureRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PrincipalTenureServiceImpl implements PrincipalTenureService {

    private final PrincipalTenureRepository principalTenureRepository;

    public PrincipalTenureServiceImpl(PrincipalTenureRepository principalTenureRepository) {
        this.principalTenureRepository = principalTenureRepository;
    }

    @Override
    public List<PrincipalTenure> findAll() {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public PrincipalTenure findById(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public PrincipalTenure create(PrincipalTenure entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public PrincipalTenure update(UUID id, PrincipalTenure entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void delete(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }
}
