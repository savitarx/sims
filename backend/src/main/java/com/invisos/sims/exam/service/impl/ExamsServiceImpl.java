package com.invisos.sims.exam.service.impl;

import com.invisos.sims.academic.model.AcademicYears;
import com.invisos.sims.academic.repository.AcademicYearsRepository;
import com.invisos.sims.common.enums.ExamStatus;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import com.invisos.sims.exam.dto.request.ExamRequestDto;
import com.invisos.sims.exam.mapper.ExamMapper;
import com.invisos.sims.exam.model.Exams;
import com.invisos.sims.exam.repository.ExamsRepository;
import com.invisos.sims.exam.service.ExamsService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ExamsServiceImpl implements ExamsService {

    private final ExamsRepository examsRepository;
    private final AcademicYearsRepository academicYearsRepository;
    private final ExamMapper examMapper;

    public ExamsServiceImpl(ExamsRepository examsRepository,
                            AcademicYearsRepository academicYearsRepository,
                            ExamMapper examMapper) {
        this.examsRepository = examsRepository;
        this.academicYearsRepository = academicYearsRepository;
        this.examMapper = examMapper;
    }

    @Override
    public List<Exams> findAll() {
        return examsRepository.findAll();
    }

    @Override
    public Exams findById(UUID id) {
        return examsRepository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("Exam not found with id: " + id));
    }

    @Override
    public Exams create(ExamRequestDto dto) {

        if (examsRepository.existsByExamNameAndAcademicYearAcademicYearId(
                dto.getExamName(), dto.getAcademicYearId())) {
            throw new IllegalArgumentException("Exam already exists for this academic year.");
        }

        AcademicYears academicYear = academicYearsRepository
                .findByAcademicYearId(dto.getAcademicYearId())
                .orElseThrow(() -> new ResourceNotFoundException(
                                "Academic Year not found with id: " + dto.getAcademicYearId()));

        Exams exam = examMapper.toEntity(dto);
        exam.setAcademicYear(academicYear);
        if (exam.getStatus() == null) {
            exam.setStatus(ExamStatus.DRAFT);
        }
        return examsRepository.save(exam);
    }

    @Override
    public Exams update(UUID id, ExamRequestDto dto) {

        Exams existingExam = findById(id);
        if (existingExam.getStatus() == ExamStatus.PUBLISHED) {
            throw new IllegalStateException("Published exams cannot be modified.");
        }

        AcademicYears academicYear = academicYearsRepository
                .findByAcademicYearId(dto.getAcademicYearId())
                .orElseThrow(() -> new ResourceNotFoundException(
                                "Academic Year not found with id: " + dto.getAcademicYearId()));

        examMapper.updateEntity(dto, existingExam);
        existingExam.setAcademicYear(academicYear);
        return examsRepository.save(existingExam);
    }

    @Override
    public Exams publish(UUID id) {

        Exams exam = findById(id);
        if (exam.getStatus() == ExamStatus.PUBLISHED) {
            throw new IllegalStateException("Exam is already published.");
        }
        exam.setStatus(ExamStatus.PUBLISHED);
        return examsRepository.save(exam);
    }

    @Override
    public void delete(UUID id) {

        Exams exam = findById(id);
        if (exam.getStatus() == ExamStatus.PUBLISHED) {
            throw new IllegalStateException("Published exams cannot be deleted.");
        }
        examsRepository.delete(exam);
    }
}