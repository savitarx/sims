package com.invisos.sims.exam.service.impl;

import com.invisos.sims.common.exception.ResourceNotFoundException;
import com.invisos.sims.exam.dto.request.MarkRequestDto;
import com.invisos.sims.exam.mapper.MarkMapper;
import com.invisos.sims.exam.model.ExamSubjects;
import com.invisos.sims.exam.model.Marks;
import com.invisos.sims.exam.repository.ExamSubjectsRepository;
import com.invisos.sims.exam.repository.MarksRepository;
import com.invisos.sims.exam.service.MarksService;
import com.invisos.sims.student.model.StudentEnrollment;
import com.invisos.sims.student.repository.StudentEnrollmentRepository;
import com.invisos.sims.teacher.model.Teachers;
import com.invisos.sims.teacher.repository.TeachersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class MarksServiceImpl implements MarksService {

    private final MarksRepository marksRepository;
    private final ExamSubjectsRepository examSubjectsRepository;
    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final TeachersRepository teachersRepository;
    private final MarkMapper markMapper;

    public MarksServiceImpl(MarksRepository marksRepository,
                            ExamSubjectsRepository examSubjectsRepository,
                            StudentEnrollmentRepository studentEnrollmentRepository,
                            TeachersRepository teachersRepository,
                            MarkMapper markMapper) {
        this.marksRepository = marksRepository;
        this.examSubjectsRepository = examSubjectsRepository;
        this.studentEnrollmentRepository = studentEnrollmentRepository;
        this.teachersRepository = teachersRepository;
        this.markMapper = markMapper;
    }

    @Override
    public List<Marks> findAll() {
        return marksRepository.findAll();
    }

    @Override
    public Marks findById(UUID id) {
        return marksRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Mark not found with id: " + id));
    }

    @Override
    public List<Marks> findByExamSubjectId(UUID examSubjectId) {
        return marksRepository.findByExamSubjectExamSubjectId(examSubjectId);
    }

    @Override
    public List<Marks> findByEnrollmentId(UUID enrollmentId) {
        return marksRepository.findByEnrollmentEnrollmentId(enrollmentId);
    }

    @Override
    public Marks create(MarkRequestDto dto) {

        if (marksRepository.existsByEnrollmentEnrollmentIdAndExamSubjectExamSubjectId(
                dto.getEnrollmentId(), dto.getExamSubjectId())) {
            throw new IllegalArgumentException("Marks already recorded for this student and exam subject.");
        }

        StudentEnrollment enrollment = findEnrollmentOrThrow(dto.getEnrollmentId());
        ExamSubjects examSubject = findExamSubjectOrThrow(dto.getExamSubjectId());
        Teachers enteredBy = findTeacherOrThrow(dto.getEnteredById());

        validateMarksWithinMax(dto.getMarksObtained(), examSubject);

        Marks mark = markMapper.toEntity(dto);
        mark.setEnrollment(enrollment);
        mark.setExamSubject(examSubject);
        mark.setEnteredBy(enteredBy);
        return marksRepository.save(mark);
    }

    @Override
    public Marks update(UUID id, MarkRequestDto dto) {

        Marks existingMark = findById(id);

        StudentEnrollment enrollment = findEnrollmentOrThrow(dto.getEnrollmentId());
        ExamSubjects examSubject = findExamSubjectOrThrow(dto.getExamSubjectId());
        Teachers enteredBy = findTeacherOrThrow(dto.getEnteredById());

        validateMarksWithinMax(dto.getMarksObtained(), examSubject);

        boolean mappingChanged = isMappingChanged(existingMark, dto);
        if (mappingChanged && marksRepository.existsByEnrollmentEnrollmentIdAndExamSubjectExamSubjectId(
                dto.getEnrollmentId(), dto.getExamSubjectId())) {
            throw new IllegalArgumentException("Marks already recorded for this student and exam subject.");
        }

        markMapper.updateEntity(dto, existingMark);
        existingMark.setEnrollment(enrollment);
        existingMark.setExamSubject(examSubject);
        existingMark.setEnteredBy(enteredBy);
        return marksRepository.save(existingMark);
    }

    @Override
    public void delete(UUID id) {
        Marks mark = findById(id);
        marksRepository.delete(mark);
    }

    private void validateMarksWithinMax(BigDecimal marksObtained, ExamSubjects examSubject) {
        Integer maxMarks = examSubject.getMaxMarks();
        if (maxMarks != null && marksObtained.compareTo(BigDecimal.valueOf(maxMarks)) > 0) {
            throw new IllegalArgumentException(
                    "Marks obtained cannot exceed the maximum marks (" + maxMarks + ") for this exam subject.");
        }
    }

    private boolean isMappingChanged(Marks existing, MarkRequestDto dto) {
        UUID currentEnrollmentId = existing.getEnrollment() == null
                ? null : existing.getEnrollment().getEnrollmentId();
        UUID currentExamSubjectId = existing.getExamSubject() == null
                ? null : existing.getExamSubject().getExamSubjectId();

        return !dto.getEnrollmentId().equals(currentEnrollmentId)
                || !dto.getExamSubjectId().equals(currentExamSubjectId);
    }

    private StudentEnrollment findEnrollmentOrThrow(UUID enrollmentId) {
        return studentEnrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student enrollment not found with id: " + enrollmentId));
    }

    private ExamSubjects findExamSubjectOrThrow(UUID examSubjectId) {
        return examSubjectsRepository.findByExamSubjectId(examSubjectId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Exam subject not found with id: " + examSubjectId));
    }

    private Teachers findTeacherOrThrow(UUID teacherId) {
        return teachersRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Teacher not found with id: " + teacherId));
    }
}
