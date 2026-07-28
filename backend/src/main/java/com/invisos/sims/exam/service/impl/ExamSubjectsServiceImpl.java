package com.invisos.sims.exam.service.impl;

import com.invisos.sims.academic.model.Classes;
import com.invisos.sims.academic.model.Subjects;
import com.invisos.sims.academic.repository.ClassesRepository;
import com.invisos.sims.academic.repository.SubjectsRepository;
import com.invisos.sims.common.enums.ExamStatus;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import com.invisos.sims.exam.dto.request.ExamSubjectRequestDto;
import com.invisos.sims.exam.mapper.ExamSubjectMapper;
import com.invisos.sims.exam.model.ExamSubjects;
import com.invisos.sims.exam.model.Exams;
import com.invisos.sims.exam.repository.ExamSubjectsRepository;
import com.invisos.sims.exam.repository.ExamsRepository;
import com.invisos.sims.exam.service.ExamSubjectsService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ExamSubjectsServiceImpl implements ExamSubjectsService {

    private final ExamSubjectsRepository examSubjectsRepository;
    private final ExamsRepository examsRepository;
    private final SubjectsRepository subjectsRepository;
    private final ClassesRepository classesRepository;
    private final ExamSubjectMapper examSubjectMapper;

    public ExamSubjectsServiceImpl(ExamSubjectsRepository examSubjectsRepository,
                                   ExamsRepository examsRepository,
                                   SubjectsRepository subjectsRepository,
                                   ClassesRepository classesRepository,
                                   ExamSubjectMapper examSubjectMapper) {
        this.examSubjectsRepository = examSubjectsRepository;
        this.examsRepository = examsRepository;
        this.subjectsRepository = subjectsRepository;
        this.classesRepository = classesRepository;
        this.examSubjectMapper = examSubjectMapper;
    }

    @Override
    public List<ExamSubjects> findAll() {
        return examSubjectsRepository.findAll();
    }

    @Override
    public ExamSubjects findById(UUID id) {
        return examSubjectsRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Exam subject not found with id: " + id));
    }

    @Override
    public List<ExamSubjects> findByExamId(UUID examId) {
        return examSubjectsRepository.findByExamExamId(examId);
    }

    @Override
    public ExamSubjects create(ExamSubjectRequestDto dto) {

        if (examSubjectsRepository.existsByExamExamIdAndSubjectSubjectIdAndSchoolClassClassId(
                dto.getExamId(), dto.getSubjectId(), dto.getClassId())) {
            throw new IllegalArgumentException("Subject is already mapped to this exam for the given class.");
        }

        Exams exam = findExamOrThrow(dto.getExamId());
        if (exam.getStatus() == ExamStatus.PUBLISHED) {
            throw new IllegalStateException("Subjects cannot be added to a published exam.");
        }

        Subjects subject = findSubjectOrThrow(dto.getSubjectId());
        Classes schoolClass = findClassOrThrow(dto.getClassId());

        ExamSubjects examSubject = examSubjectMapper.toEntity(dto);
        examSubject.setExam(exam);
        examSubject.setSubject(subject);
        examSubject.setSchoolClass(schoolClass);
        return examSubjectsRepository.save(examSubject);
    }

    @Override
    public ExamSubjects update(UUID id, ExamSubjectRequestDto dto) {

        ExamSubjects existingExamSubject = findById(id);
        if (existingExamSubject.getExam() != null
                && existingExamSubject.getExam().getStatus() == ExamStatus.PUBLISHED) {
            throw new IllegalStateException("Subjects of a published exam cannot be modified.");
        }

        Exams exam = findExamOrThrow(dto.getExamId());
        if (exam.getStatus() == ExamStatus.PUBLISHED) {
            throw new IllegalStateException("Subjects cannot be moved to a published exam.");
        }

        Subjects subject = findSubjectOrThrow(dto.getSubjectId());
        Classes schoolClass = findClassOrThrow(dto.getClassId());

        boolean mappingChanged = isMappingChanged(existingExamSubject, dto);
        if (mappingChanged && examSubjectsRepository.existsByExamExamIdAndSubjectSubjectIdAndSchoolClassClassId(
                dto.getExamId(), dto.getSubjectId(), dto.getClassId())) {
            throw new IllegalArgumentException("Subject is already mapped to this exam for the given class.");
        }

        examSubjectMapper.updateEntity(dto, existingExamSubject);
        existingExamSubject.setExam(exam);
        existingExamSubject.setSubject(subject);
        existingExamSubject.setSchoolClass(schoolClass);
        return examSubjectsRepository.save(existingExamSubject);
    }

    @Override
    public void delete(UUID id) {

        ExamSubjects examSubject = findById(id);
        if (examSubject.getExam() != null
                && examSubject.getExam().getStatus() == ExamStatus.PUBLISHED) {
            throw new IllegalStateException("Subjects of a published exam cannot be deleted.");
        }
        examSubjectsRepository.delete(examSubject);
    }

    private boolean isMappingChanged(ExamSubjects existing, ExamSubjectRequestDto dto) {
        UUID currentExamId = existing.getExam() == null ? null : existing.getExam().getExamId();
        UUID currentSubjectId = existing.getSubject() == null ? null : existing.getSubject().getSubjectId();
        UUID currentClassId = existing.getSchoolClass() == null ? null : existing.getSchoolClass().getClassId();

        return !dto.getExamId().equals(currentExamId)
                || !dto.getSubjectId().equals(currentSubjectId)
                || !dto.getClassId().equals(currentClassId);
    }

    private Exams findExamOrThrow(UUID examId) {
        return examsRepository.findByExamId(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with id: " + examId));
    }

    private Subjects findSubjectOrThrow(UUID subjectId) {
        return subjectsRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + subjectId));
    }

    private Classes findClassOrThrow(UUID classId) {
        return classesRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + classId));
    }
}
