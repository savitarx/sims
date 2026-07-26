package com.invisos.sims.fee.service;

import com.invisos.sims.fee.model.StudentFeeStatus;
import com.invisos.sims.fee.repository.StudentFeeStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StudentFeeStatusServiceImpl implements StudentFeeStatusService {

    private final StudentFeeStatusRepository studentFeeStatusRepository;

    public StudentFeeStatusServiceImpl(StudentFeeStatusRepository studentFeeStatusRepository) {
        this.studentFeeStatusRepository = studentFeeStatusRepository;
    }

    @Override
    public List<StudentFeeStatus> findAll() {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public StudentFeeStatus findById(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public StudentFeeStatus create(StudentFeeStatus entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public StudentFeeStatus update(UUID id, StudentFeeStatus entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void delete(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }
}
