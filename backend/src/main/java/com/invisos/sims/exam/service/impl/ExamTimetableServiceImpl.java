package com.invisos.sims.exam.service.impl;

import com.invisos.sims.academic.model.AcademicYears;
import com.invisos.sims.admin.model.AdminStaff;
import com.invisos.sims.admin.repository.AdminStaffRepository;
import com.invisos.sims.common.enums.ExamStatus;
import com.invisos.sims.common.exception.BusinessRuleViolationException;
import com.invisos.sims.common.exception.DuplicateResourceException;
import com.invisos.sims.common.exception.InvalidRequestException;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import com.invisos.sims.exam.dto.request.ExamTimetableRequestDto;
import com.invisos.sims.exam.mapper.ExamTimetableMapper;
import com.invisos.sims.exam.model.ExamSubjects;
import com.invisos.sims.exam.model.ExamTimetable;
import com.invisos.sims.exam.model.Exams;
import com.invisos.sims.exam.repository.ExamSubjectsRepository;
import com.invisos.sims.exam.repository.ExamTimetableRepository;
import com.invisos.sims.exam.service.ExamTimetableService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ExamTimetableServiceImpl implements ExamTimetableService {

    private final ExamTimetableRepository examTimetableRepository;
    private final ExamSubjectsRepository examSubjectsRepository;
    private final AdminStaffRepository adminStaffRepository;
    private final ExamTimetableMapper examTimetableMapper;

    public ExamTimetableServiceImpl(ExamTimetableRepository examTimetableRepository,
                                    ExamSubjectsRepository examSubjectsRepository,
                                    AdminStaffRepository adminStaffRepository,
                                    ExamTimetableMapper examTimetableMapper) {
        this.examTimetableRepository = examTimetableRepository;
        this.examSubjectsRepository = examSubjectsRepository;
        this.adminStaffRepository = adminStaffRepository;
        this.examTimetableMapper = examTimetableMapper;
    }

    @Override
    public Page<ExamTimetable> findAll(Pageable pageable) {
        return examTimetableRepository.findAll(pageable);
    }

    @Override
    public ExamTimetable findById(UUID id) {
        return examTimetableRepository.findByExamTimetableId(id).orElseThrow(() ->
                new ResourceNotFoundException("Exam timetable not found with id: " + id));
    }

    @Override
    public List<ExamTimetable> findByExamId(UUID examId) {
        return examTimetableRepository.findByExamSubjectExamExamIdOrderByExamDateAscExamTimeAsc(examId);
    }

    @Override
    public ExamTimetable create(ExamTimetableRequestDto dto) {

        if (examTimetableRepository.existsByExamSubjectExamSubjectId(dto.getExamSubjectId())) {
            throw new DuplicateResourceException("This exam subject is already scheduled.");
        }

        ExamSubjects examSubject = findExamSubjectOrThrow(dto.getExamSubjectId());
        AdminStaff actor = findAdminOrThrow(dto.getActorId());

        validateExamNotPublished(examSubject, "Timetable entries cannot be added to a published exam.");
        validateDateWithinAcademicYear(dto.getExamDate(), examSubject);

        ExamTimetable examTimetable = examTimetableMapper.toEntity(dto);
        examTimetable.setExamSubject(examSubject);
        examTimetable.setUpdatedBy(actor);
        return examTimetableRepository.save(examTimetable);
    }

    @Override
    public ExamTimetable update(UUID id, ExamTimetableRequestDto dto) {

        ExamTimetable existingTimetable = findById(id);
        validateExamNotPublished(existingTimetable.getExamSubject(),
                "Timetable entries of a published exam cannot be modified.");

        ExamSubjects examSubject = findExamSubjectOrThrow(dto.getExamSubjectId());
        AdminStaff actor = findAdminOrThrow(dto.getActorId());

        validateExamNotPublished(examSubject, "Timetable entries cannot be moved to a published exam.");
        validateDateWithinAcademicYear(dto.getExamDate(), examSubject);

        if (isExamSubjectChanged(existingTimetable, dto)
                && examTimetableRepository.existsByExamSubjectExamSubjectId(dto.getExamSubjectId())) {
            throw new DuplicateResourceException("This exam subject is already scheduled.");
        }

        examTimetableMapper.updateEntity(dto, existingTimetable);
        existingTimetable.setExamSubject(examSubject);
        existingTimetable.setUpdatedBy(actor);
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
            throw new BusinessRuleViolationException(message);
        }
    }

    /** An exam cannot be scheduled outside the academic year it belongs to. */
    private void validateDateWithinAcademicYear(LocalDate examDate, ExamSubjects examSubject) {

        Exams exam = examSubject.getExam();
        AcademicYears academicYear = exam == null ? null : exam.getAcademicYear();
        if (academicYear == null || examDate == null) {
            return;
        }

        LocalDate start = academicYear.getStartDate();
        LocalDate end = academicYear.getEndDate();

        if ((start != null && examDate.isBefore(start)) || (end != null && examDate.isAfter(end))) {
            throw new InvalidRequestException(
                    "Exam date " + examDate + " falls outside the academic year ("
                            + start + " to " + end + ").");
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

    private AdminStaff findAdminOrThrow(UUID adminId) {
        return adminStaffRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Admin staff not found with id: " + adminId));
    }
}
