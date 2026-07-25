package com.invisos.sims.fee.service;

import com.invisos.sims.fee.model.StudentFeeStatus;

import java.util.List;
import java.util.UUID;

public interface StudentFeeStatusService {

    List<StudentFeeStatus> findAll();

    StudentFeeStatus findById(UUID id);

    StudentFeeStatus create(StudentFeeStatus entity);

    StudentFeeStatus update(UUID id, StudentFeeStatus entity);

    void delete(UUID id);
}
