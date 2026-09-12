package com.invisos.sims.fee.service;

import com.invisos.sims.fee.dto.request.FeeStatusSheetSaveRequestDto;
import com.invisos.sims.fee.dto.request.StudentFeeStatusRequestDto;
import com.invisos.sims.fee.dto.response.FeeStatusSheetResponseDto;
import com.invisos.sims.fee.model.StudentFeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface StudentFeeStatusService {

    Page<StudentFeeStatus> findAll(Pageable pageable);

    StudentFeeStatus findById(UUID id);

    List<StudentFeeStatus> findByEnrollmentId(UUID enrollmentId);

    /** Students who have not fully paid, optionally narrowed by class and year. */
    Page<StudentFeeStatus> findDefaulters(UUID classId, UUID academicYearId, Pageable pageable);

    StudentFeeStatus create(StudentFeeStatusRequestDto request);

    StudentFeeStatus update(UUID id, StudentFeeStatusRequestDto request);

    void delete(UUID id);

    /** Fee collection screen: one fee, one section, every student's status. */
    FeeStatusSheetResponseDto getStatusSheet(UUID feeId, UUID sectionId);

    /** Transactional bulk upsert of a whole fee status sheet. */
    FeeStatusSheetResponseDto saveStatusSheet(FeeStatusSheetSaveRequestDto request);
}
