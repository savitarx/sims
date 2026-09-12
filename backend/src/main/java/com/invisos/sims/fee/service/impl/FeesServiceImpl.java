package com.invisos.sims.fee.service.impl;

import com.invisos.sims.academic.model.AcademicYears;
import com.invisos.sims.academic.model.Classes;
import com.invisos.sims.academic.repository.AcademicYearsRepository;
import com.invisos.sims.academic.repository.ClassesRepository;
import com.invisos.sims.admin.model.AdminStaff;
import com.invisos.sims.admin.repository.AdminStaffRepository;
import com.invisos.sims.common.exception.BusinessRuleViolationException;
import com.invisos.sims.common.exception.DuplicateResourceException;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import com.invisos.sims.fee.dto.request.FeeRequestDto;
import com.invisos.sims.fee.mapper.FeeMapper;
import com.invisos.sims.fee.model.Fees;
import com.invisos.sims.fee.repository.FeesRepository;
import com.invisos.sims.fee.repository.StudentFeeStatusRepository;
import com.invisos.sims.fee.service.FeesService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class FeesServiceImpl implements FeesService {

    private final FeesRepository feesRepository;
    private final StudentFeeStatusRepository studentFeeStatusRepository;
    private final ClassesRepository classesRepository;
    private final AcademicYearsRepository academicYearsRepository;
    private final AdminStaffRepository adminStaffRepository;
    private final FeeMapper feeMapper;

    public FeesServiceImpl(FeesRepository feesRepository,
                           StudentFeeStatusRepository studentFeeStatusRepository,
                           ClassesRepository classesRepository,
                           AcademicYearsRepository academicYearsRepository,
                           AdminStaffRepository adminStaffRepository,
                           FeeMapper feeMapper) {
        this.feesRepository = feesRepository;
        this.studentFeeStatusRepository = studentFeeStatusRepository;
        this.classesRepository = classesRepository;
        this.academicYearsRepository = academicYearsRepository;
        this.adminStaffRepository = adminStaffRepository;
        this.feeMapper = feeMapper;
    }

    @Override
    public Page<Fees> findAll(UUID classId, UUID academicYearId, Pageable pageable) {

        if (classId != null && academicYearId != null) {
            return feesRepository.findBySchoolClassClassIdAndAcademicYearAcademicYearId(
                    classId, academicYearId, pageable);
        }
        if (classId != null) {
            return feesRepository.findBySchoolClassClassId(classId, pageable);
        }
        if (academicYearId != null) {
            return feesRepository.findByAcademicYearAcademicYearId(academicYearId, pageable);
        }
        return feesRepository.findAll(pageable);
    }

    @Override
    public Fees findById(UUID id) {
        return feesRepository.findByFeeId(id).orElseThrow(() ->
                new ResourceNotFoundException("Fee not found with id: " + id));
    }

    @Override
    public Fees create(FeeRequestDto dto) {

        if (feesRepository.existsBySchoolClassClassIdAndAcademicYearAcademicYearIdAndTermName(
                dto.getClassId(), dto.getAcademicYearId(), dto.getTermName())) {
            throw new DuplicateResourceException(
                    "A fee is already defined for this class, academic year and term.");
        }

        Classes schoolClass = findClassOrThrow(dto.getClassId());
        AcademicYears academicYear = findAcademicYearOrThrow(dto.getAcademicYearId());
        AdminStaff actor = findAdminOrThrow(dto.getActorId());

        Fees fee = feeMapper.toEntity(dto);
        fee.setSchoolClass(schoolClass);
        fee.setAcademicYear(academicYear);
        fee.setUpdatedBy(actor);
        return feesRepository.save(fee);
    }

    @Override
    public Fees update(UUID id, FeeRequestDto dto) {

        Fees existingFee = findById(id);

        if (feesRepository.existsBySchoolClassClassIdAndAcademicYearAcademicYearIdAndTermNameAndFeeIdNot(
                dto.getClassId(), dto.getAcademicYearId(), dto.getTermName(), id)) {
            throw new DuplicateResourceException(
                    "A fee is already defined for this class, academic year and term.");
        }

        // Re-pointing a fee that already has statuses would silently move those
        // students' records onto a different class or year.
        if (isDefinitionChanged(existingFee, dto)
                && studentFeeStatusRepository.existsByFeeFeeId(id)) {
            throw new BusinessRuleViolationException(
                    "This fee cannot be re-defined because student fee statuses are already recorded "
                            + "against it. Remove those statuses first.");
        }

        Classes schoolClass = findClassOrThrow(dto.getClassId());
        AcademicYears academicYear = findAcademicYearOrThrow(dto.getAcademicYearId());
        AdminStaff actor = findAdminOrThrow(dto.getActorId());

        feeMapper.updateEntity(dto, existingFee);
        existingFee.setSchoolClass(schoolClass);
        existingFee.setAcademicYear(academicYear);
        existingFee.setUpdatedBy(actor);
        return feesRepository.save(existingFee);
    }

    @Override
    public void delete(UUID id) {

        Fees fee = findById(id);
        if (studentFeeStatusRepository.existsByFeeFeeId(id)) {
            throw new BusinessRuleViolationException(
                    "Fee cannot be deleted while student fee statuses are recorded against it.");
        }
        feesRepository.delete(fee);
    }

    private boolean isDefinitionChanged(Fees existing, FeeRequestDto dto) {
        UUID currentClassId = existing.getSchoolClass() == null
                ? null : existing.getSchoolClass().getClassId();
        UUID currentAcademicYearId = existing.getAcademicYear() == null
                ? null : existing.getAcademicYear().getAcademicYearId();

        return !dto.getClassId().equals(currentClassId)
                || !dto.getAcademicYearId().equals(currentAcademicYearId);
    }

    private Classes findClassOrThrow(UUID classId) {
        return classesRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + classId));
    }

    private AcademicYears findAcademicYearOrThrow(UUID academicYearId) {
        return academicYearsRepository.findByAcademicYearId(academicYearId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Academic year not found with id: " + academicYearId));
    }

    private AdminStaff findAdminOrThrow(UUID adminId) {
        return adminStaffRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin staff not found with id: " + adminId));
    }
}
