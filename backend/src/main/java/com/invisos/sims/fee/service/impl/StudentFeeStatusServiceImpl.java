package com.invisos.sims.fee.service.impl;

import com.invisos.sims.academic.model.Sections;
import com.invisos.sims.academic.repository.SectionsRepository;
import com.invisos.sims.admin.model.AdminStaff;
import com.invisos.sims.admin.repository.AdminStaffRepository;
import com.invisos.sims.common.enums.FeeStatus;
import com.invisos.sims.common.enums.RecordStatus;
import com.invisos.sims.common.exception.DuplicateResourceException;
import com.invisos.sims.common.exception.InvalidRequestException;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import com.invisos.sims.common.mapper.SummaryMapper;
import com.invisos.sims.fee.dto.request.FeeStatusSheetEntryRequestDto;
import com.invisos.sims.fee.dto.request.FeeStatusSheetSaveRequestDto;
import com.invisos.sims.fee.dto.request.StudentFeeStatusRequestDto;
import com.invisos.sims.fee.dto.response.FeeStatusSheetEntryDto;
import com.invisos.sims.fee.dto.response.FeeStatusSheetResponseDto;
import com.invisos.sims.fee.mapper.FeeMapper;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentFeeStatusServiceImpl implements StudentFeeStatusService {

    /** Anything short of PAID counts as outstanding. */
    private static final List<FeeStatus> OUTSTANDING = List.of(FeeStatus.NOT_PAID, FeeStatus.PARTIAL);

    private final StudentFeeStatusRepository studentFeeStatusRepository;
    private final FeesRepository feesRepository;
    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final SectionsRepository sectionsRepository;
    private final TeachersRepository teachersRepository;
    private final AdminStaffRepository adminStaffRepository;
    private final FeeMapper feeMapper;
    private final SummaryMapper summaryMapper;

    public StudentFeeStatusServiceImpl(StudentFeeStatusRepository studentFeeStatusRepository,
                                       FeesRepository feesRepository,
                                       StudentEnrollmentRepository studentEnrollmentRepository,
                                       SectionsRepository sectionsRepository,
                                       TeachersRepository teachersRepository,
                                       AdminStaffRepository adminStaffRepository,
                                       FeeMapper feeMapper,
                                       SummaryMapper summaryMapper) {
        this.studentFeeStatusRepository = studentFeeStatusRepository;
        this.feesRepository = feesRepository;
        this.studentEnrollmentRepository = studentEnrollmentRepository;
        this.sectionsRepository = sectionsRepository;
        this.teachersRepository = teachersRepository;
        this.adminStaffRepository = adminStaffRepository;
        this.feeMapper = feeMapper;
        this.summaryMapper = summaryMapper;
    }

    // ------------------------------------------------------------------
    // CRUD
    // ------------------------------------------------------------------

    @Override
    public Page<StudentFeeStatus> findAll(Pageable pageable) {
        return studentFeeStatusRepository.findAll(pageable);
    }

    @Override
    public StudentFeeStatus findById(UUID id) {
        return studentFeeStatusRepository.findByStudentFeeStatusId(id).orElseThrow(() ->
                new ResourceNotFoundException("Student fee status not found with id: " + id));
    }

    @Override
    public List<StudentFeeStatus> findByEnrollmentId(UUID enrollmentId) {
        return studentFeeStatusRepository.findByEnrollmentEnrollmentId(enrollmentId);
    }

    @Override
    public Page<StudentFeeStatus> findDefaulters(UUID classId, UUID academicYearId, Pageable pageable) {

        if (classId != null && academicYearId != null) {
            return studentFeeStatusRepository
                    .findByStatusInAndFeeSchoolClassClassIdAndFeeAcademicYearAcademicYearId(
                            OUTSTANDING, classId, academicYearId, pageable);
        }
        if (classId != null) {
            return studentFeeStatusRepository.findByStatusInAndFeeSchoolClassClassId(
                    OUTSTANDING, classId, pageable);
        }
        if (academicYearId != null) {
            return studentFeeStatusRepository.findByStatusInAndFeeAcademicYearAcademicYearId(
                    OUTSTANDING, academicYearId, pageable);
        }
        return studentFeeStatusRepository.findByStatusIn(OUTSTANDING, pageable);
    }

    @Override
    public StudentFeeStatus create(StudentFeeStatusRequestDto dto) {

        if (studentFeeStatusRepository.existsByEnrollmentEnrollmentIdAndFeeFeeId(
                dto.getEnrollmentId(), dto.getFeeId())) {
            throw new DuplicateResourceException(
                    "Fee status is already recorded for this student and fee.");
        }

        StudentEnrollment enrollment = findEnrollmentOrThrow(dto.getEnrollmentId());
        Fees fee = findFeeOrThrow(dto.getFeeId());
        validateEnrollmentMatchesFee(enrollment, fee);

        StudentFeeStatus status = new StudentFeeStatus();
        status.setEnrollment(enrollment);
        status.setFee(fee);
        status.setStatus(dto.getStatus() == null ? FeeStatus.NOT_PAID : dto.getStatus());
        applyActor(status, dto.getUpdatedById(), dto.getUpdatedByAdminId());
        return studentFeeStatusRepository.save(status);
    }

    @Override
    public StudentFeeStatus update(UUID id, StudentFeeStatusRequestDto dto) {

        StudentFeeStatus existingStatus = findById(id);

        StudentEnrollment enrollment = findEnrollmentOrThrow(dto.getEnrollmentId());
        Fees fee = findFeeOrThrow(dto.getFeeId());
        validateEnrollmentMatchesFee(enrollment, fee);

        if (isMappingChanged(existingStatus, dto)
                && studentFeeStatusRepository.existsByEnrollmentEnrollmentIdAndFeeFeeId(
                        dto.getEnrollmentId(), dto.getFeeId())) {
            throw new DuplicateResourceException(
                    "Fee status is already recorded for this student and fee.");
        }

        existingStatus.setEnrollment(enrollment);
        existingStatus.setFee(fee);
        existingStatus.setStatus(dto.getStatus());
        applyActor(existingStatus, dto.getUpdatedById(), dto.getUpdatedByAdminId());
        return studentFeeStatusRepository.save(existingStatus);
    }

    @Override
    public void delete(UUID id) {
        StudentFeeStatus studentFeeStatus = findById(id);
        studentFeeStatusRepository.delete(studentFeeStatus);
    }

    // ------------------------------------------------------------------
    // Fee status sheet
    // ------------------------------------------------------------------

    @Override
    public FeeStatusSheetResponseDto getStatusSheet(UUID feeId, UUID sectionId) {

        Fees fee = findFeeOrThrow(feeId);
        Sections section = findSectionOrThrow(sectionId);
        validateSectionMatchesFee(section, fee);

        List<StudentEnrollment> roster = roster(sectionId);
        Map<UUID, StudentFeeStatus> statuses = statusesByEnrollment(feeId, sectionId);
        return buildSheet(fee, section, roster, statuses);
    }

    @Override
    public FeeStatusSheetResponseDto saveStatusSheet(FeeStatusSheetSaveRequestDto request) {

        Fees fee = findFeeOrThrow(request.getFeeId());
        Sections section = findSectionOrThrow(request.getSectionId());
        validateSectionMatchesFee(section, fee);

        List<StudentEnrollment> roster = roster(request.getSectionId());
        Map<UUID, StudentEnrollment> rosterById = roster.stream()
                .collect(Collectors.toMap(StudentEnrollment::getEnrollmentId, Function.identity()));

        // Validate the whole payload first so a bad row cannot half-save the sheet.
        Set<UUID> seen = new HashSet<>();
        for (FeeStatusSheetEntryRequestDto entry : request.getEntries()) {
            if (!seen.add(entry.getEnrollmentId())) {
                throw new InvalidRequestException(
                        "Duplicate entry for enrollment id: " + entry.getEnrollmentId());
            }
            if (!rosterById.containsKey(entry.getEnrollmentId())) {
                throw new InvalidRequestException(
                        "Enrollment " + entry.getEnrollmentId() + " does not belong to the given section.");
            }
        }

        Map<UUID, StudentFeeStatus> existing =
                statusesByEnrollment(request.getFeeId(), request.getSectionId());

        for (FeeStatusSheetEntryRequestDto entry : request.getEntries()) {
            StudentFeeStatus current = existing.get(entry.getEnrollmentId());

            if (current == null) {
                current = new StudentFeeStatus();
                current.setEnrollment(rosterById.get(entry.getEnrollmentId()));
                current.setFee(fee);
            }
            current.setStatus(entry.getStatus());
            applyActor(current, request.getUpdatedById(), request.getUpdatedByAdminId());
            existing.put(entry.getEnrollmentId(), studentFeeStatusRepository.save(current));
        }

        return buildSheet(fee, section, roster, existing);
    }

    // ------------------------------------------------------------------
    // Builders
    // ------------------------------------------------------------------

    private FeeStatusSheetResponseDto buildSheet(Fees fee,
                                                 Sections section,
                                                 List<StudentEnrollment> roster,
                                                 Map<UUID, StudentFeeStatus> statuses) {

        List<FeeStatusSheetEntryDto> entries = new ArrayList<>(roster.size());
        long paid = 0;
        long partial = 0;
        long notPaid = 0;
        long unrecorded = 0;

        for (StudentEnrollment enrollment : roster) {
            StudentFeeStatus status = statuses.get(enrollment.getEnrollmentId());

            if (status == null) {
                unrecorded++;
            } else if (status.getStatus() == FeeStatus.PAID) {
                paid++;
            } else if (status.getStatus() == FeeStatus.PARTIAL) {
                partial++;
            } else {
                notPaid++;
            }

            entries.add(FeeStatusSheetEntryDto.builder()
                    .student(summaryMapper.toStudentSummary(enrollment))
                    .studentFeeStatusId(status == null ? null : status.getStudentFeeStatusId())
                    .status(status == null ? null : status.getStatus())
                    .updatedBy(status == null ? null : resolveActor(status))
                    .updatedAt(status == null ? null : status.getUpdatedAt())
                    .build());
        }

        return FeeStatusSheetResponseDto.builder()
                .fee(feeMapper.toSummary(fee))
                .section(summaryMapper.toSectionSummary(section))
                .totalStudents(roster.size())
                .paidCount(paid)
                .partialCount(partial)
                .notPaidCount(notPaid)
                .unrecordedCount(unrecorded)
                .entries(entries)
                .build();
    }

    private com.invisos.sims.common.dto.summary.ActorSummaryDto resolveActor(StudentFeeStatus status) {
        return status.getUpdatedBy() != null
                ? summaryMapper.toActor(status.getUpdatedBy())
                : summaryMapper.toActor(status.getUpdatedByAdmin());
    }

    private Map<UUID, StudentFeeStatus> statusesByEnrollment(UUID feeId, UUID sectionId) {
        return studentFeeStatusRepository
                .findByFeeFeeIdAndEnrollmentSectionSectionId(feeId, sectionId).stream()
                .filter(status -> status.getEnrollment() != null)
                .collect(Collectors.toMap(
                        status -> status.getEnrollment().getEnrollmentId(),
                        Function.identity(),
                        (first, second) -> first));
    }

    private List<StudentEnrollment> roster(UUID sectionId) {
        return studentEnrollmentRepository
                .findBySectionSectionIdAndStatusOrderByRollNumberAsc(sectionId, RecordStatus.ACTIVE);
    }

    // ------------------------------------------------------------------
    // Validation
    // ------------------------------------------------------------------

    /** Exactly one of teacher/admin must be supplied as the acting user. */
    private void applyActor(StudentFeeStatus status, UUID teacherId, UUID adminId) {

        if ((teacherId == null) == (adminId == null)) {
            throw new InvalidRequestException(
                    "Exactly one of updatedById (teacher) or updatedByAdminId must be provided.");
        }

        if (teacherId != null) {
            Teachers teacher = teachersRepository.findById(teacherId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Teacher not found with id: " + teacherId));
            status.setUpdatedBy(teacher);
            status.setUpdatedByAdmin(null);
        } else {
            AdminStaff admin = adminStaffRepository.findById(adminId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Admin staff not found with id: " + adminId));
            status.setUpdatedByAdmin(admin);
            status.setUpdatedBy(null);
        }
    }

    /** A student can only hold a status for a fee defined for their own class. */
    private void validateEnrollmentMatchesFee(StudentEnrollment enrollment, Fees fee) {

        UUID feeClassId = fee.getSchoolClass() == null ? null : fee.getSchoolClass().getClassId();
        Sections section = enrollment.getSection();
        UUID studentClassId = section == null || section.getSchoolClass() == null
                ? null : section.getSchoolClass().getClassId();

        if (feeClassId != null && studentClassId != null && !feeClassId.equals(studentClassId)) {
            throw new InvalidRequestException(
                    "The student is not enrolled in the class this fee belongs to.");
        }
    }

    private void validateSectionMatchesFee(Sections section, Fees fee) {

        UUID feeClassId = fee.getSchoolClass() == null ? null : fee.getSchoolClass().getClassId();
        UUID sectionClassId = section.getSchoolClass() == null
                ? null : section.getSchoolClass().getClassId();

        if (feeClassId != null && sectionClassId != null && !feeClassId.equals(sectionClassId)) {
            throw new InvalidRequestException(
                    "The section does not belong to the class this fee belongs to.");
        }
    }

    private boolean isMappingChanged(StudentFeeStatus existing, StudentFeeStatusRequestDto dto) {
        UUID currentEnrollmentId = existing.getEnrollment() == null
                ? null : existing.getEnrollment().getEnrollmentId();
        UUID currentFeeId = existing.getFee() == null ? null : existing.getFee().getFeeId();

        return !dto.getEnrollmentId().equals(currentEnrollmentId)
                || !dto.getFeeId().equals(currentFeeId);
    }

    // ------------------------------------------------------------------
    // Lookups
    // ------------------------------------------------------------------

    private StudentEnrollment findEnrollmentOrThrow(UUID enrollmentId) {
        return studentEnrollmentRepository.findByEnrollmentId(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student enrollment not found with id: " + enrollmentId));
    }

    private Fees findFeeOrThrow(UUID feeId) {
        return feesRepository.findByFeeId(feeId)
                .orElseThrow(() -> new ResourceNotFoundException("Fee not found with id: " + feeId));
    }

    private Sections findSectionOrThrow(UUID sectionId) {
        return sectionsRepository.findBySectionId(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with id: " + sectionId));
    }
}
