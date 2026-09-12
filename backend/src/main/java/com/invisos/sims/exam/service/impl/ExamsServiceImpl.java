package com.invisos.sims.exam.service.impl;

import com.invisos.sims.academic.model.AcademicYears;
import com.invisos.sims.academic.repository.AcademicYearsRepository;
import com.invisos.sims.admin.model.AdminStaff;
import com.invisos.sims.admin.repository.AdminStaffRepository;
import com.invisos.sims.common.enums.ExamStatus;
import com.invisos.sims.common.exception.BusinessRuleViolationException;
import com.invisos.sims.common.exception.DuplicateResourceException;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import com.invisos.sims.common.mapper.SummaryMapper;
import com.invisos.sims.exam.dto.request.ExamRequestDto;
import com.invisos.sims.exam.dto.response.ExamOverviewResponseDto;
import com.invisos.sims.exam.dto.response.ExamSubjectProgressDto;
import com.invisos.sims.exam.dto.response.PublishPreviewResponseDto;
import com.invisos.sims.exam.mapper.ExamMapper;
import com.invisos.sims.exam.model.ExamSubjects;
import com.invisos.sims.exam.model.ExamTimetable;
import com.invisos.sims.exam.model.Exams;
import com.invisos.sims.exam.repository.ExamSubjectsRepository;
import com.invisos.sims.exam.repository.ExamTimetableRepository;
import com.invisos.sims.exam.repository.ExamsRepository;
import com.invisos.sims.exam.repository.MarksRepository;
import com.invisos.sims.exam.repository.projection.ExamSubjectMarkCount;
import com.invisos.sims.exam.service.ExamsService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExamsServiceImpl implements ExamsService {

    private final ExamsRepository examsRepository;
    private final ExamSubjectsRepository examSubjectsRepository;
    private final ExamTimetableRepository examTimetableRepository;
    private final MarksRepository marksRepository;
    private final AcademicYearsRepository academicYearsRepository;
    private final AdminStaffRepository adminStaffRepository;
    private final ExamMapper examMapper;
    private final SummaryMapper summaryMapper;

    public ExamsServiceImpl(ExamsRepository examsRepository,
                            ExamSubjectsRepository examSubjectsRepository,
                            ExamTimetableRepository examTimetableRepository,
                            MarksRepository marksRepository,
                            AcademicYearsRepository academicYearsRepository,
                            AdminStaffRepository adminStaffRepository,
                            ExamMapper examMapper,
                            SummaryMapper summaryMapper) {
        this.examsRepository = examsRepository;
        this.examSubjectsRepository = examSubjectsRepository;
        this.examTimetableRepository = examTimetableRepository;
        this.marksRepository = marksRepository;
        this.academicYearsRepository = academicYearsRepository;
        this.adminStaffRepository = adminStaffRepository;
        this.examMapper = examMapper;
        this.summaryMapper = summaryMapper;
    }

    @Override
    public Page<Exams> findAll(UUID academicYearId, ExamStatus status, Pageable pageable) {

        if (academicYearId != null && status != null) {
            return examsRepository.findByAcademicYearAcademicYearIdAndStatus(academicYearId, status, pageable);
        }
        if (academicYearId != null) {
            return examsRepository.findByAcademicYearAcademicYearId(academicYearId, pageable);
        }
        if (status != null) {
            return examsRepository.findByStatus(status, pageable);
        }
        return examsRepository.findAll(pageable);
    }

    @Override
    public Exams findById(UUID id) {
        return examsRepository.findByExamId(id).orElseThrow(() ->
                new ResourceNotFoundException("Exam not found with id: " + id));
    }

    @Override
    public Exams create(ExamRequestDto dto) {

        if (examsRepository.existsByExamNameAndAcademicYearAcademicYearId(
                dto.getExamName(), dto.getAcademicYearId())) {
            throw new DuplicateResourceException(
                    "An exam named '" + dto.getExamName() + "' already exists for this academic year.");
        }

        AcademicYears academicYear = findAcademicYearOrThrow(dto.getAcademicYearId());
        AdminStaff actor = findAdminOrThrow(dto.getActorId());

        Exams exam = examMapper.toEntity(dto);
        exam.setAcademicYear(academicYear);
        exam.setCreatedBy(actor);
        exam.setUpdatedBy(actor);
        if (exam.getStatus() == null) {
            exam.setStatus(ExamStatus.DRAFT);
        }
        return examsRepository.save(exam);
    }

    @Override
    public Exams update(UUID id, ExamRequestDto dto) {

        Exams existingExam = findById(id);
        if (existingExam.getStatus() == ExamStatus.PUBLISHED) {
            throw new BusinessRuleViolationException("Published exams cannot be modified.");
        }

        if (examsRepository.existsByExamNameAndAcademicYearAcademicYearIdAndExamIdNot(
                dto.getExamName(), dto.getAcademicYearId(), id)) {
            throw new DuplicateResourceException(
                    "An exam named '" + dto.getExamName() + "' already exists for this academic year.");
        }

        AcademicYears academicYear = findAcademicYearOrThrow(dto.getAcademicYearId());
        AdminStaff actor = findAdminOrThrow(dto.getActorId());

        // Status is only changed through publish(), never through a plain update.
        ExamStatus currentStatus = existingExam.getStatus();
        examMapper.updateEntity(dto, existingExam);
        existingExam.setStatus(currentStatus);
        existingExam.setAcademicYear(academicYear);
        existingExam.setUpdatedBy(actor);
        return examsRepository.save(existingExam);
    }

    @Override
    public Exams publish(UUID id) {

        Exams exam = findById(id);
        if (exam.getStatus() == ExamStatus.PUBLISHED) {
            throw new BusinessRuleViolationException("Exam is already published.");
        }

        List<String> blockers = collectPublishBlockers(exam);
        if (!blockers.isEmpty()) {
            throw new BusinessRuleViolationException(
                    "Exam cannot be published: " + String.join(" ", blockers));
        }

        exam.setStatus(ExamStatus.PUBLISHED);
        return examsRepository.save(exam);
    }

    @Override
    public void delete(UUID id) {

        Exams exam = findById(id);
        if (exam.getStatus() == ExamStatus.PUBLISHED) {
            throw new BusinessRuleViolationException("Published exams cannot be deleted.");
        }
        if (marksRepository.existsByExamSubjectExamExamId(id)) {
            throw new BusinessRuleViolationException(
                    "Exam cannot be deleted because marks have already been recorded against it.");
        }
        if (examSubjectsRepository.countByExamExamId(id) > 0) {
            throw new BusinessRuleViolationException(
                    "Exam cannot be deleted while subjects are mapped to it. Remove the subjects first.");
        }
        examsRepository.delete(exam);
    }

    @Override
    public ExamOverviewResponseDto getOverview(UUID examId) {

        Exams exam = findById(examId);
        List<ExamSubjects> examSubjects = examSubjectsRepository.findByExamExamId(examId);
        Map<UUID, ExamTimetable> timetableBySubject = timetableBySubject(examId);
        Map<UUID, Long> markCounts = markCountsBySubject(examId);

        List<ExamSubjectProgressDto> subjects = examSubjects.stream()
                .map(examSubject -> toProgress(examSubject, timetableBySubject, markCounts))
                .toList();

        int scheduled = (int) subjects.stream().filter(ExamSubjectProgressDto::isScheduled).count();

        return ExamOverviewResponseDto.builder()
                .examId(exam.getExamId())
                .examName(exam.getExamName())
                .status(exam.getStatus())
                .academicYear(summaryMapper.toAcademicYearSummary(exam.getAcademicYear()))
                .createdBy(summaryMapper.toActor(exam.getCreatedBy()))
                .totalSubjects(examSubjects.size())
                .scheduledSubjects(scheduled)
                .readyToPublish(collectPublishBlockers(exam).isEmpty())
                .subjects(subjects)
                .build();
    }

    @Override
    public PublishPreviewResponseDto getPublishPreview(UUID examId) {

        Exams exam = findById(examId);
        List<String> blockers = collectPublishBlockers(exam);
        List<String> warnings = new ArrayList<>();

        long totalSubjects = examSubjectsRepository.countByExamExamId(examId);
        long scheduledSubjects = examTimetableRepository.countByExamSubjectExamExamId(examId);

        if (exam.getStatus() == ExamStatus.PUBLISHED) {
            blockers.add("Exam is already published.");
        }

        Map<UUID, Long> markCounts = markCountsBySubject(examId);
        long subjectsWithoutMarks = examSubjectsRepository.findByExamExamId(examId).stream()
                .filter(examSubject -> markCounts.getOrDefault(examSubject.getExamSubjectId(), 0L) == 0L)
                .count();
        if (subjectsWithoutMarks > 0) {
            warnings.add(subjectsWithoutMarks + " subject(s) have no marks entered yet.");
        }

        return PublishPreviewResponseDto.builder()
                .examId(exam.getExamId())
                .examName(exam.getExamName())
                .readyToPublish(blockers.isEmpty())
                .totalSubjects((int) totalSubjects)
                .scheduledSubjects((int) scheduledSubjects)
                .blockers(blockers)
                .warnings(warnings)
                .build();
    }

    /** Conditions that must hold before an exam can be published. */
    private List<String> collectPublishBlockers(Exams exam) {

        List<String> blockers = new ArrayList<>();
        UUID examId = exam.getExamId();

        long subjectCount = examSubjectsRepository.countByExamExamId(examId);
        if (subjectCount == 0) {
            blockers.add("No subjects have been mapped to this exam.");
            return blockers;
        }

        long timetabledCount = examTimetableRepository.countByExamSubjectExamExamId(examId);
        if (timetabledCount < subjectCount) {
            blockers.add((subjectCount - timetabledCount) + " subject(s) have not been scheduled in the timetable.");
        }
        return blockers;
    }

    private ExamSubjectProgressDto toProgress(ExamSubjects examSubject,
                                              Map<UUID, ExamTimetable> timetableBySubject,
                                              Map<UUID, Long> markCounts) {

        ExamTimetable slot = timetableBySubject.get(examSubject.getExamSubjectId());

        return ExamSubjectProgressDto.builder()
                .examSubjectId(examSubject.getExamSubjectId())
                .subject(summaryMapper.toSubjectSummary(examSubject.getSubject()))
                .schoolClass(summaryMapper.toClassSummary(examSubject.getSchoolClass()))
                .maxMarks(examSubject.getMaxMarks())
                .scheduled(slot != null)
                .examDate(slot == null ? null : slot.getExamDate())
                .examTime(slot == null ? null : slot.getExamTime())
                .marksEntered(markCounts.getOrDefault(examSubject.getExamSubjectId(), 0L))
                .build();
    }

    private Map<UUID, ExamTimetable> timetableBySubject(UUID examId) {
        return examTimetableRepository.findByExamSubjectExamExamIdOrderByExamDateAscExamTimeAsc(examId).stream()
                .filter(slot -> slot.getExamSubject() != null)
                .collect(Collectors.toMap(
                        slot -> slot.getExamSubject().getExamSubjectId(),
                        Function.identity(),
                        (first, second) -> first));
    }

    private Map<UUID, Long> markCountsBySubject(UUID examId) {
        Map<UUID, Long> counts = new HashMap<>();
        for (ExamSubjectMarkCount row : marksRepository.countMarksPerExamSubject(examId)) {
            counts.put(row.getExamSubjectId(), row.getTotal());
        }
        return counts;
    }

    private AcademicYears findAcademicYearOrThrow(UUID academicYearId) {
        return academicYearsRepository.findByAcademicYearId(academicYearId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Academic year not found with id: " + academicYearId));
    }

    private AdminStaff findAdminOrThrow(UUID adminId) {
        return adminStaffRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Admin staff not found with id: " + adminId));
    }
}
