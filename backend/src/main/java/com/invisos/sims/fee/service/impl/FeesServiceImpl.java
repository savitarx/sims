package com.invisos.sims.fee.service.impl;

import com.invisos.sims.academic.model.AcademicYears;
import com.invisos.sims.academic.model.Classes;
import com.invisos.sims.academic.repository.AcademicYearsRepository;
import com.invisos.sims.academic.repository.ClassesRepository;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import com.invisos.sims.fee.dto.request.FeeRequestDto;
import com.invisos.sims.fee.mapper.FeeMapper;
import com.invisos.sims.fee.model.Fees;
import com.invisos.sims.fee.repository.FeesRepository;
import com.invisos.sims.fee.repository.StudentFeeStatusRepository;
import com.invisos.sims.fee.service.FeesService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class FeesServiceImpl implements FeesService {

    private final FeesRepository feesRepository;
    private final StudentFeeStatusRepository studentFeeStatusRepository;
    private final ClassesRepository classesRepository;
    private final AcademicYearsRepository academicYearsRepository;
    private final FeeMapper feeMapper;

    public FeesServiceImpl(FeesRepository feesRepository,
                           StudentFeeStatusRepository studentFeeStatusRepository,
                           ClassesRepository classesRepository,
                           AcademicYearsRepository academicYearsRepository,
                           FeeMapper feeMapper) {
        this.feesRepository = feesRepository;
        this.studentFeeStatusRepository = studentFeeStatusRepository;
        this.classesRepository = classesRepository;
        this.academicYearsRepository = academicYearsRepository;
        this.feeMapper = feeMapper;
    }

    @Override
    public List<Fees> findAll() {
        return feesRepository.findAll();
    }

    @Override
    public Fees findById(UUID id) {
        return feesRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Fee not found with id: " + id));
    }

    @Override
    public Fees create(FeeRequestDto dto) {

        if (feesRepository.existsBySchoolClassClassIdAndAcademicYearAcademicYearIdAndTermName(
                dto.getClassId(), dto.getAcademicYearId(), dto.getTermName())) {
            throw new IllegalArgumentException(
                    "A fee is already defined for this class, academic year and term.");
        }

        Classes schoolClass = findClassOrThrow(dto.getClassId());
        AcademicYears academicYear = findAcademicYearOrThrow(dto.getAcademicYearId());

        Fees fee = feeMapper.toEntity(dto);
        fee.setSchoolClass(schoolClass);
        fee.setAcademicYear(academicYear);
        return feesRepository.save(fee);
    }

    @Override
    public Fees update(UUID id, FeeRequestDto dto) {

        Fees existingFee = findById(id);

        Classes schoolClass = findClassOrThrow(dto.getClassId());
        AcademicYears academicYear = findAcademicYearOrThrow(dto.getAcademicYearId());

        boolean definitionChanged = isDefinitionChanged(existingFee, dto);
        if (definitionChanged && feesRepository.existsBySchoolClassClassIdAndAcademicYearAcademicYearIdAndTermName(
                dto.getClassId(), dto.getAcademicYearId(), dto.getTermName())) {
            throw new IllegalArgumentException(
                    "A fee is already defined for this class, academic year and term.");
        }

        feeMapper.updateEntity(dto, existingFee);
        existingFee.setSchoolClass(schoolClass);
        existingFee.setAcademicYear(academicYear);
        return feesRepository.save(existingFee);
    }

    @Override
    public void delete(UUID id) {

        Fees fee = findById(id);
        if (studentFeeStatusRepository.existsByFeeFeeId(id)) {
            throw new IllegalStateException(
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
                || !dto.getAcademicYearId().equals(currentAcademicYearId)
                || !dto.getTermName().equals(existing.getTermName());
    }

    private Classes findClassOrThrow(UUID classId) {
        return classesRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + classId));
    }

    private AcademicYears findAcademicYearOrThrow(UUID academicYearId) {
        return academicYearsRepository.findById(academicYearId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Academic year not found with id: " + academicYearId));
    }
}
