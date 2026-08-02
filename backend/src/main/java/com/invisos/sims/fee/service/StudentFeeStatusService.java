package com.invisos.sims.fee.service;

import com.invisos.sims.fee.dto.request.StudentFeeStatusRequestDto;
import com.invisos.sims.fee.model.StudentFeeStatus;

import java.util.List;
import java.util.UUID;

public interface StudentFeeStatusService {

    List<StudentFeeStatus> findAll();

    StudentFeeStatus findById(UUID id);

    List<StudentFeeStatus> findByEnrollmentId(UUID enrollmentId);

    StudentFeeStatus create(StudentFeeStatusRequestDto request);

    StudentFeeStatus update(UUID id, StudentFeeStatusRequestDto request);

    void delete(UUID id);
}
