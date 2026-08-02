package com.invisos.sims.fee.service.impl;

import com.invisos.sims.common.enums.FeeStatus;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import com.invisos.sims.fee.dto.request.StudentFeeStatusRequestDto;
import com.invisos.sims.fee.mapper.StudentFeeStatusMapper;
import com.invisos.sims.fee.model.Fees;
import com.invisos.sims.fee.model.StudentFeeStatus;
import com.invisos.sims.fee.repository.FeesRepository;
import com.invisos.sims.fee.repository.StudentFeeStatusRepository;
import com.invisos.sims.fee.service.StudentFeeStatusService;
import com.invisos.sims.student.model.StudentEnrollment;
import com.invisos.sims.student.repository.StudentEnrollmentRepository;
import com.invisos.sims.teacher.model.Teachers;
import com.invisos.sims.teacher.repository.TeachersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class StudentFeeStatusServiceImpl implements StudentFeeStatusService {

    private final StudentFeeStatusRepository studentFeeStatusRepository;
    private final FeesRepository feesRepository;
    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final TeachersRepository teachersRepository;
    private final StudentFeeStatusMapper studentFeeStatusMapper;

    public StudentFeeStatusServiceImpl(StudentFeeStatusRepository studentFeeStatusRepository,
                                       FeesRepository feesRepository,
                                       StudentEnrollmentRepository studentEnrollmentRepository,
                                       TeachersRepository teachersRepository,
                                       StudentFeeStatusMapper studentFeeStatusMapper) {
        this.studentFeeStatusRepository = studentFeeStatusRepository;
        this.feesRepository = feesRepository;
        this.studentEnrollmentRepository = studentEnrollmentRepository;
        this.teachersRepository = teachersRepository;
        this.studentFeeStatusMapper = studentFeeStatusMapper;
    }

    @Override
    public List<StudentFeeStatus> findAll() {
        return studentFeeStatusRepository.findAll();
    }

    @Override
    public StudentFeeStatus findById(UUID id) {
        return studentFeeStatusRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Student fee status not found with id: " + id));
    }

    @Override
    public List<StudentFeeStatus> findByEnrollmentId(UUID enrollmentId) {
        return studentFeeStatusRepository.findByEnrollmentEnrollmentId(enrollmentId);
    }

    @Override
    public StudentFeeStatus create(StudentFeeStatusRequestDto dto) {

        if (studentFeeStatusRepository.existsByEnrollmentEnrollmentIdAndFeeFeeId(
                dto.getEnrollmentId(), dto.getFeeId())) {
            throw new IllegalArgumentException("Fee status is already recorded for this student and fee.");
        }

        StudentEnrollment enrollment = findEnrollmentOrThrow(dto.getEnrollmentId());
        Fees fee = findFeeOrThrow(dto.getFeeId());
        Teachers updatedBy = findTeacherOrThrow(dto.getUpdatedById());

        StudentFeeStatus studentFeeStatus = studentFeeStatusMapper.toEntity(dto);
        studentFeeStatus.setEnrollment(enrollment);
        studentFeeStatus.setFee(fee);
        studentFeeStatus.setUpdatedBy(updatedBy);
        if (studentFeeStatus.getStatus() == null) {
            studentFeeStatus.setStatus(FeeStatus.NOT_PAID);
        }
        return studentFeeStatusRepository.save(studentFeeStatus);
    }

    @Override
    public StudentFeeStatus update(UUID id, StudentFeeStatusRequestDto dto) {

        StudentFeeStatus existingStatus = findById(id);

        StudentEnrollment enrollment = findEnrollmentOrThrow(dto.getEnrollmentId());
        Fees fee = findFeeOrThrow(dto.getFeeId());
        Teachers updatedBy = findTeacherOrThrow(dto.getUpdatedById());

        boolean mappingChanged = isMappingChanged(existingStatus, dto);
        if (mappingChanged && studentFeeStatusRepository.existsByEnrollmentEnrollmentIdAndFeeFeeId(
                dto.getEnrollmentId(), dto.getFeeId())) {
            throw new IllegalArgumentException("Fee status is already recorded for this student and fee.");
        }

        studentFeeStatusMapper.updateEntity(dto, existingStatus);
        existingStatus.setEnrollment(enrollment);
        existingStatus.setFee(fee);
        existingStatus.setUpdatedBy(updatedBy);
        return studentFeeStatusRepository.save(existingStatus);
    }

    @Override
    public void delete(UUID id) {
        StudentFeeStatus studentFeeStatus = findById(id);
        studentFeeStatusRepository.delete(studentFeeStatus);
    }

    private boolean isMappingChanged(StudentFeeStatus existing, StudentFeeStatusRequestDto dto) {
        UUID currentEnrollmentId = existing.getEnrollment() == null
                ? null : existing.getEnrollment().getEnrollmentId();
        UUID currentFeeId = existing.getFee() == null
                ? null : existing.getFee().getFeeId();

        return !dto.getEnrollmentId().equals(currentEnrollmentId)
                || !dto.getFeeId().equals(currentFeeId);
    }

    private StudentEnrollment findEnrollmentOrThrow(UUID enrollmentId) {
        return studentEnrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student enrollment not found with id: " + enrollmentId));
    }

    private Fees findFeeOrThrow(UUID feeId) {
        return feesRepository.findByFeeId(feeId)
                .orElseThrow(() -> new ResourceNotFoundException("Fee not found with id: " + feeId));
    }

    private Teachers findTeacherOrThrow(UUID teacherId) {
        return teachersRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));
    }
}
