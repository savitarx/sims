package com.invisos.sims.exam.service.impl;

import com.invisos.sims.common.enums.ExamStatus;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import com.invisos.sims.exam.dto.request.ExamTimetableRequestDto;
import com.invisos.sims.exam.mapper.ExamTimetableMapper;
import com.invisos.sims.exam.model.ExamSubjects;
import com.invisos.sims.exam.model.ExamTimetable;
import com.invisos.sims.exam.repository.ExamSubjectsRepository;
import com.invisos.sims.exam.repository.ExamTimetableRepository;
import com.invisos.sims.exam.service.ExamTimetableService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ExamTimetableServiceImpl implements ExamTimetableService {

    private final ExamTimetableRepository examTimetableRepository;
    private final ExamSubjectsRepository examSubjectsRepository;
    private final ExamTimetableMapper examTimetableMapper;

    public ExamTimetableServiceImpl(ExamTimetableRepository examTimetableRepository,
                                    ExamSubjectsRepository examSubjectsRepository,
                                    ExamTimetableMapper examTimetableMapper) {
        this.examTimetableRepository = examTimetableRepository;
        this.examSubjectsRepository = examSubjectsRepository;
        this.examTimetableMapper = examTimetableMapper;
    }

    @Override
    public List<ExamTimetable> findAll() {
        return examTimetableRepository.findAll();
    }

    @Override
    public ExamTimetable findById(UUID id) {
        return examTimetableRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Exam timetable not found with id: " + id));
    }

    @Override
    public List<ExamTimetable> findByExamId(UUID examId) {
        return examTimetableRepository.findByExamSubjectExamExamId(examId);
    }

    @Override
    public ExamTimetable create(ExamTimetableRequestDto dto) {

        if (examTimetableRepository.existsByExamSubjectExamSubjectId(dto.getExamSubjectId())) {
            throw new IllegalArgumentException("This exam subject is already scheduled.");
        }

        ExamSubjects examSubject = findExamSubjectOrThrow(dto.getExamSubjectId());
        validateExamNotPublished(examSubject, "Timetable entries cannot be added to a published exam.");

        ExamTimetable examTimetable = examTimetableMapper.toEntity(dto);
        examTimetable.setExamSubject(examSubject);
        return examTimetableRepository.save(examTimetable);
    }

    @Override
    public ExamTimetable update(UUID id, ExamTimetableRequestDto dto) {

        ExamTimetable existingTimetable = findById(id);
        validateExamNotPublished(existingTimetable.getExamSubject(),
                "Timetable entries of a published exam cannot be modified.");

        ExamSubjects examSubject = findExamSubjectOrThrow(dto.getExamSubjectId());
        validateExamNotPublished(examSubject, "Timetable entries cannot be moved to a published exam.");

        boolean examSubjectChanged = isExamSubjectChanged(existingTimetable, dto);
        if (examSubjectChanged && examTimetableRepository.existsByExamSubjectExamSubjectId(dto.getExamSubjectId())) {
            throw new IllegalArgumentException("This exam subject is already scheduled.");
        }

        examTimetableMapper.updateEntity(dto, existingTimetable);
        existingTimetable.setExamSubject(examSubject);
        return examTimetableRepository.save(existingTimetable);
    }

    @Override
    public void delete(UUID id) {

        ExamTimetable examTimetable = findById(id);
        validateExamNotPublished(examTimetable.getExamSubject(),
                "Timetable entries of a published exam cannot be deleted.");
        examTimetableRepository.delete(examTimetable);
    }

    private void validateExamNotPublished(ExamSubjects examSubject, String message) {
        if (examSubject != null
                && examSubject.getExam() != null
                && examSubject.getExam().getStatus() == ExamStatus.PUBLISHED) {
            throw new IllegalStateException(message);
        }
    }

    private boolean isExamSubjectChanged(ExamTimetable existing, ExamTimetableRequestDto dto) {
        UUID currentExamSubjectId = existing.getExamSubject() == null
                ? null : existing.getExamSubject().getExamSubjectId();

        return !dto.getExamSubjectId().equals(currentExamSubjectId);
    }

    private ExamSubjects findExamSubjectOrThrow(UUID examSubjectId) {
        return examSubjectsRepository.findByExamSubjectId(examSubjectId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Exam subject not found with id: " + examSubjectId));
    }
}
