package com.invisos.sims.exam.service.impl;

import com.invisos.sims.academic.model.Classes;
import com.invisos.sims.academic.model.Subjects;
import com.invisos.sims.academic.repository.ClassesRepository;
import com.invisos.sims.academic.repository.SubjectsRepository;
import com.invisos.sims.admin.model.AdminStaff;
import com.invisos.sims.admin.repository.AdminStaffRepository;
import com.invisos.sims.common.enums.ExamStatus;
import com.invisos.sims.common.exception.BusinessRuleViolationException;
import com.invisos.sims.common.exception.DuplicateResourceException;
import com.invisos.sims.common.exception.InvalidRequestException;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import com.invisos.sims.exam.dto.request.ExamSubjectRequestDto;
import com.invisos.sims.exam.mapper.ExamSubjectMapper;
import com.invisos.sims.exam.model.ExamSubjects;
import com.invisos.sims.exam.model.Exams;
import com.invisos.sims.exam.repository.ExamSubjectsRepository;
import com.invisos.sims.exam.repository.ExamsRepository;
import com.invisos.sims.exam.repository.MarksRepository;
import com.invisos.sims.exam.service.ExamSubjectsService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ExamSubjectsServiceImpl implements ExamSubjectsService {

    private final ExamSubjectsRepository examSubjectsRepository;
    private final ExamsRepository examsRepository;
    private final SubjectsRepository subjectsRepository;
    private final ClassesRepository classesRepository;
    private final MarksRepository marksRepository;
    private final AdminStaffRepository adminStaffRepository;
    private final ExamSubjectMapper examSubjectMapper;

    public ExamSubjectsServiceImpl(ExamSubjectsRepository examSubjectsRepository,
                                   ExamsRepository examsRepository,
                                   SubjectsRepository subjectsRepository,
                                   ClassesRepository classesRepository,
                                   MarksRepository marksRepository,
                                   AdminStaffRepository adminStaffRepository,
                                   ExamSubjectMapper examSubjectMapper) {
        this.examSubjectsRepository = examSubjectsRepository;
        this.examsRepository = examsRepository;
        this.subjectsRepository = subjectsRepository;
        this.classesRepository = classesRepository;
        this.marksRepository = marksRepository;
        this.adminStaffRepository = adminStaffRepository;
        this.examSubjectMapper = examSubjectMapper;
    }

    @Override
    public Page<ExamSubjects> findAll(Pageable pageable) {
        return examSubjectsRepository.findAll(pageable);
    }

    @Override
    public ExamSubjects findById(UUID id) {
        return examSubjectsRepository.findByExamSubjectId(id).orElseThrow(() ->
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
            throw new DuplicateResourceException(
                    "This subject is already mapped to the exam for the given class.");
        }

        Exams exam = findExamOrThrow(dto.getExamId());
        if (exam.getStatus() == ExamStatus.PUBLISHED) {
            throw new BusinessRuleViolationException("Subjects cannot be added to a published exam.");
        }

        Subjects subject = findSubjectOrThrow(dto.getSubjectId());
        Classes schoolClass = findClassOrThrow(dto.getClassId());
        AdminStaff actor = findAdminOrThrow(dto.getActorId());

        ExamSubjects examSubject = examSubjectMapper.toEntity(dto);
        examSubject.setExam(exam);
        examSubject.setSubject(subject);
        examSubject.setSchoolClass(schoolClass);
        examSubject.setUpdatedBy(actor);
        return examSubjectsRepository.save(examSubject);
    }

    @Override
    public ExamSubjects update(UUID id, ExamSubjectRequestDto dto) {

        ExamSubjects existingExamSubject = findById(id);
        validateExamNotPublished(existingExamSubject.getExam(),
                "Subjects of a published exam cannot be modified.");

        Exams exam = findExamOrThrow(dto.getExamId());
        validateExamNotPublished(exam, "Subjects cannot be moved to a published exam.");

        Subjects subject = findSubjectOrThrow(dto.getSubjectId());
        Classes schoolClass = findClassOrThrow(dto.getClassId());
        AdminStaff actor = findAdminOrThrow(dto.getActorId());

        boolean mappingChanged = isMappingChanged(existingExamSubject, dto);
        if (mappingChanged) {
            // Re-pointing a mapping that already holds marks would silently
            // re-attribute those marks to a different subject or class.
            if (marksRepository.existsByExamSubjectExamSubjectId(id)) {
                throw new BusinessRuleViolationException(
                        "This exam subject cannot be re-mapped because marks have already been recorded "
                                + "against it. Delete the marks first.");
            }
            if (examSubjectsRepository.existsByExamExamIdAndSubjectSubjectIdAndSchoolClassClassId(
                    dto.getExamId(), dto.getSubjectId(), dto.getClassId())) {
                throw new DuplicateResourceException(
                        "This subject is already mapped to the exam for the given class.");
            }
        }

        validateMaxMarksNotBelowRecordedMarks(id, dto);

        examSubjectMapper.updateEntity(dto, existingExamSubject);
        existingExamSubject.setExam(exam);
        existingExamSubject.setSubject(subject);
        existingExamSubject.setSchoolClass(schoolClass);
        existingExamSubject.setUpdatedBy(actor);
        return examSubjectsRepository.save(existingExamSubject);
    }

    @Override
    public void delete(UUID id) {

        ExamSubjects examSubject = findById(id);
        validateExamNotPublished(examSubject.getExam(),
                "Subjects of a published exam cannot be deleted.");

        if (marksRepository.existsByExamSubjectExamSubjectId(id)) {
            throw new BusinessRuleViolationException(
                    "This exam subject cannot be deleted because marks have already been recorded against it.");
        }
        examSubjectsRepository.delete(examSubject);
    }

    private void validateExamNotPublished(Exams exam, String message) {
        if (exam != null && exam.getStatus() == ExamStatus.PUBLISHED) {
            throw new BusinessRuleViolationException(message);
        }
    }

    /** Lowering the maximum below a mark already awarded would invalidate that mark. */
    private void validateMaxMarksNotBelowRecordedMarks(UUID examSubjectId, ExamSubjectRequestDto dto) {

        if (dto.getMaxMarks() == null) {
            return;
        }
        BigDecimal highestRecorded = marksRepository.findHighestMarkObtained(examSubjectId);
        if (highestRecorded == null) {
            return;
        }
        if (BigDecimal.valueOf(dto.getMaxMarks()).compareTo(highestRecorded) < 0) {
            throw new InvalidRequestException(
                    "Maximum marks cannot be lowered to " + dto.getMaxMarks()
                            + " because a mark of " + highestRecorded + " has already been recorded.");
        }
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
        return subjectsRepository.findBySubjectId(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + subjectId));
    }

    private Classes findClassOrThrow(UUID classId) {
        return classesRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + classId));
    }

    private AdminStaff findAdminOrThrow(UUID adminId) {
        return adminStaffRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin staff not found with id: " + adminId));
    }
}
