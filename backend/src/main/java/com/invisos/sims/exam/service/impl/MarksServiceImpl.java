package com.invisos.sims.exam.service.impl;

import com.invisos.sims.academic.model.Sections;
import com.invisos.sims.academic.repository.SectionsRepository;
import com.invisos.sims.common.enums.ExamStatus;
import com.invisos.sims.common.enums.RecordStatus;
import com.invisos.sims.common.exception.BusinessRuleViolationException;
import com.invisos.sims.common.exception.DuplicateResourceException;
import com.invisos.sims.common.exception.InvalidRequestException;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import com.invisos.sims.common.mapper.SummaryMapper;
import com.invisos.sims.exam.dto.request.MarkRequestDto;
import com.invisos.sims.exam.dto.request.MarksSheetEntryRequestDto;
import com.invisos.sims.exam.dto.request.MarksSheetSaveRequestDto;
import com.invisos.sims.exam.dto.response.MarksSheetEntryDto;
import com.invisos.sims.exam.dto.response.MarksSheetResponseDto;
import com.invisos.sims.exam.dto.response.StudentResultResponseDto;
import com.invisos.sims.exam.dto.response.StudentResultSubjectDto;
import com.invisos.sims.exam.mapper.ExamMapper;
import com.invisos.sims.exam.mapper.MarkMapper;
import com.invisos.sims.exam.model.ExamSubjects;
import com.invisos.sims.exam.model.Exams;
import com.invisos.sims.exam.model.Marks;
import com.invisos.sims.exam.repository.ExamSubjectsRepository;
import com.invisos.sims.exam.repository.ExamsRepository;
import com.invisos.sims.exam.repository.MarksRepository;
import com.invisos.sims.exam.service.MarksService;
import com.invisos.sims.student.model.StudentEnrollment;
import com.invisos.sims.student.repository.StudentEnrollmentRepository;
import com.invisos.sims.teacher.model.Teachers;
import com.invisos.sims.teacher.repository.TeachersRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class MarksServiceImpl implements MarksService {

    private static final int PERCENTAGE_SCALE = 2;

    private final MarksRepository marksRepository;
    private final ExamsRepository examsRepository;
    private final ExamSubjectsRepository examSubjectsRepository;
    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final SectionsRepository sectionsRepository;
    private final TeachersRepository teachersRepository;
    private final MarkMapper markMapper;
    private final ExamMapper examMapper;
    private final SummaryMapper summaryMapper;

    public MarksServiceImpl(MarksRepository marksRepository,
                            ExamsRepository examsRepository,
                            ExamSubjectsRepository examSubjectsRepository,
                            StudentEnrollmentRepository studentEnrollmentRepository,
                            SectionsRepository sectionsRepository,
                            TeachersRepository teachersRepository,
                            MarkMapper markMapper,
                            ExamMapper examMapper,
                            SummaryMapper summaryMapper) {
        this.marksRepository = marksRepository;
        this.examsRepository = examsRepository;
        this.examSubjectsRepository = examSubjectsRepository;
        this.studentEnrollmentRepository = studentEnrollmentRepository;
        this.sectionsRepository = sectionsRepository;
        this.teachersRepository = teachersRepository;
        this.markMapper = markMapper;
        this.examMapper = examMapper;
        this.summaryMapper = summaryMapper;
    }

    // ------------------------------------------------------------------
    // CRUD
    // ------------------------------------------------------------------

    @Override
    public Page<Marks> findAll(Pageable pageable) {
        return marksRepository.findAll(pageable);
    }

    @Override
    public Marks findById(UUID id) {
        return marksRepository.findByMarkId(id).orElseThrow(() ->
                new ResourceNotFoundException("Mark not found with id: " + id));
    }

    @Override
    public Page<Marks> findByExamSubjectId(UUID examSubjectId, Pageable pageable) {
        return marksRepository.findByExamSubjectExamSubjectId(examSubjectId, pageable);
    }

    @Override
    public List<Marks> findByEnrollmentId(UUID enrollmentId) {
        return marksRepository.findByEnrollmentEnrollmentId(enrollmentId);
    }

    @Override
    public Marks create(MarkRequestDto dto) {

        if (marksRepository.existsByEnrollmentEnrollmentIdAndExamSubjectExamSubjectId(
                dto.getEnrollmentId(), dto.getExamSubjectId())) {
            throw new DuplicateResourceException(
                    "Marks are already recorded for this student and exam subject.");
        }

        StudentEnrollment enrollment = findEnrollmentOrThrow(dto.getEnrollmentId());
        ExamSubjects examSubject = findExamSubjectOrThrow(dto.getExamSubjectId());
        Teachers teacher = findTeacherOrThrow(dto.getEnteredById());

        validateExamOpenForMarks(examSubject);
        validateEnrollmentMatchesExamSubject(enrollment, examSubject);
        validateMarksWithinMax(dto.getMarksObtained(), examSubject);

        Marks mark = markMapper.toEntity(dto);
        mark.setEnrollment(enrollment);
        mark.setExamSubject(examSubject);
        mark.setEnteredBy(teacher);
        mark.setUpdatedBy(teacher);
        return marksRepository.save(mark);
    }

    @Override
    public Marks update(UUID id, MarkRequestDto dto) {

        Marks existingMark = findById(id);

        StudentEnrollment enrollment = findEnrollmentOrThrow(dto.getEnrollmentId());
        ExamSubjects examSubject = findExamSubjectOrThrow(dto.getExamSubjectId());
        Teachers teacher = findTeacherOrThrow(dto.getEnteredById());

        validateExamOpenForMarks(examSubject);
        validateEnrollmentMatchesExamSubject(enrollment, examSubject);
        validateMarksWithinMax(dto.getMarksObtained(), examSubject);

        if (isMappingChanged(existingMark, dto)
                && marksRepository.existsByEnrollmentEnrollmentIdAndExamSubjectExamSubjectId(
                        dto.getEnrollmentId(), dto.getExamSubjectId())) {
            throw new DuplicateResourceException(
                    "Marks are already recorded for this student and exam subject.");
        }

        markMapper.updateEntity(dto, existingMark);
        existingMark.setEnrollment(enrollment);
        existingMark.setExamSubject(examSubject);
        existingMark.setUpdatedBy(teacher);
        return marksRepository.save(existingMark);
    }

    @Override
    public void delete(UUID id) {
        Marks mark = findById(id);
        validateExamOpenForMarks(mark.getExamSubject());
        marksRepository.delete(mark);
    }

    // ------------------------------------------------------------------
    // Marks sheet
    // ------------------------------------------------------------------

    @Override
    public MarksSheetResponseDto getMarksSheet(UUID examSubjectId, UUID sectionId) {

        ExamSubjects examSubject = findExamSubjectOrThrow(examSubjectId);
        Sections section = findSectionOrThrow(sectionId);
        validateSectionMatchesExamSubject(section, examSubject);

        List<StudentEnrollment> roster = roster(sectionId);
        Map<UUID, Marks> marksByEnrollment = marksRepository
                .findByExamSubjectExamSubjectIdAndEnrollmentSectionSectionId(examSubjectId, sectionId).stream()
                .filter(mark -> mark.getEnrollment() != null)
                .collect(Collectors.toMap(
                        mark -> mark.getEnrollment().getEnrollmentId(),
                        Function.identity(),
                        (first, second) -> first));

        return buildSheet(examSubject, section, roster, marksByEnrollment);
    }

    @Override
    public MarksSheetResponseDto saveMarksSheet(MarksSheetSaveRequestDto request) {

        ExamSubjects examSubject = findExamSubjectOrThrow(request.getExamSubjectId());
        Sections section = findSectionOrThrow(request.getSectionId());
        Teachers teacher = findTeacherOrThrow(request.getEnteredById());

        validateExamOpenForMarks(examSubject);
        validateSectionMatchesExamSubject(section, examSubject);

        List<StudentEnrollment> roster = roster(request.getSectionId());
        Map<UUID, StudentEnrollment> rosterById = roster.stream()
                .collect(Collectors.toMap(StudentEnrollment::getEnrollmentId, Function.identity()));

        // Validate the whole payload before writing anything, so a bad row cannot
        // leave the sheet half-saved.
        Set<UUID> seen = new HashSet<>();
        for (MarksSheetEntryRequestDto entry : request.getEntries()) {
            if (!seen.add(entry.getEnrollmentId())) {
                throw new InvalidRequestException(
                        "Duplicate entry for enrollment id: " + entry.getEnrollmentId());
            }
            if (!rosterById.containsKey(entry.getEnrollmentId())) {
                throw new InvalidRequestException(
                        "Enrollment " + entry.getEnrollmentId() + " does not belong to the given section.");
            }
            validateMarksWithinMax(entry.getMarksObtained(), examSubject);
        }

        Map<UUID, Marks> existing = marksRepository
                .findByExamSubjectExamSubjectIdAndEnrollmentSectionSectionId(
                        request.getExamSubjectId(), request.getSectionId()).stream()
                .filter(mark -> mark.getEnrollment() != null)
                .collect(Collectors.toMap(
                        mark -> mark.getEnrollment().getEnrollmentId(),
                        Function.identity(),
                        (first, second) -> first));

        for (MarksSheetEntryRequestDto entry : request.getEntries()) {
            Marks current = existing.get(entry.getEnrollmentId());

            if (entry.getMarksObtained() == null) {
                // A cleared cell removes any previously recorded mark.
                if (current != null) {
                    marksRepository.delete(current);
                    existing.remove(entry.getEnrollmentId());
                }
                continue;
            }

            if (current == null) {
                Marks mark = Marks.builder()
                        .enrollment(rosterById.get(entry.getEnrollmentId()))
                        .examSubject(examSubject)
                        .marksObtained(entry.getMarksObtained())
                        .enteredBy(teacher)
                        .updatedBy(teacher)
                        .build();
                existing.put(entry.getEnrollmentId(), marksRepository.save(mark));
            } else {
                current.setMarksObtained(entry.getMarksObtained());
                current.setUpdatedBy(teacher);
                existing.put(entry.getEnrollmentId(), marksRepository.save(current));
            }
        }

        return buildSheet(examSubject, section, roster, existing);
    }

    // ------------------------------------------------------------------
    // Results
    // ------------------------------------------------------------------

    @Override
    public StudentResultResponseDto getStudentResult(UUID examId, UUID enrollmentId) {

        Exams exam = findExamOrThrow(examId);
        StudentEnrollment enrollment = findEnrollmentOrThrow(enrollmentId);

        List<Marks> marks = marksRepository
                .findByExamSubjectExamExamIdAndEnrollmentEnrollmentId(examId, enrollmentId);

        return buildResult(exam, enrollment, examSubjectsForEnrollment(examId, enrollment), marks);
    }

    @Override
    public List<StudentResultResponseDto> getSectionResults(UUID examId, UUID sectionId) {

        Exams exam = findExamOrThrow(examId);
        Sections section = findSectionOrThrow(sectionId);

        List<StudentEnrollment> roster = roster(sectionId);
        List<ExamSubjects> examSubjects = section.getSchoolClass() == null
                ? List.of()
                : examSubjectsRepository.findByExamExamIdAndSchoolClassClassId(
                        examId, section.getSchoolClass().getClassId());

        Map<UUID, List<Marks>> marksByEnrollment = marksRepository
                .findByExamSubjectExamExamIdAndEnrollmentSectionSectionId(examId, sectionId).stream()
                .filter(mark -> mark.getEnrollment() != null)
                .collect(Collectors.groupingBy(mark -> mark.getEnrollment().getEnrollmentId()));

        List<StudentResultResponseDto> results = new ArrayList<>(roster.size());
        for (StudentEnrollment enrollment : roster) {
            results.add(buildResult(exam, enrollment, examSubjects,
                    marksByEnrollment.getOrDefault(enrollment.getEnrollmentId(), List.of())));
        }
        return results;
    }

    // ------------------------------------------------------------------
    // Builders
    // ------------------------------------------------------------------

    private MarksSheetResponseDto buildSheet(ExamSubjects examSubject,
                                             Sections section,
                                             List<StudentEnrollment> roster,
                                             Map<UUID, Marks> marksByEnrollment) {

        List<MarksSheetEntryDto> entries = new ArrayList<>(roster.size());
        int entered = 0;

        for (StudentEnrollment enrollment : roster) {
            Marks mark = marksByEnrollment.get(enrollment.getEnrollmentId());
            if (mark != null) {
                entered++;
            }
            entries.add(MarksSheetEntryDto.builder()
                    .student(summaryMapper.toStudentSummary(enrollment))
                    .markId(mark == null ? null : mark.getMarkId())
                    .marksObtained(mark == null ? null : mark.getMarksObtained())
                    .enteredBy(mark == null ? null : summaryMapper.toActor(mark.getEnteredBy()))
                    .updatedAt(mark == null ? null : mark.getUpdatedAt())
                    .build());
        }

        boolean published = examSubject.getExam() != null
                && examSubject.getExam().getStatus() == ExamStatus.PUBLISHED;

        return MarksSheetResponseDto.builder()
                .examSubjectId(examSubject.getExamSubjectId())
                .exam(examMapper.toSummary(examSubject.getExam()))
                .subject(summaryMapper.toSubjectSummary(examSubject.getSubject()))
                .section(summaryMapper.toSectionSummary(section))
                .maxMarks(examSubject.getMaxMarks())
                .totalStudents(roster.size())
                .marksEntered(entered)
                .complete(!roster.isEmpty() && entered == roster.size())
                .editable(!published)
                .entries(entries)
                .build();
    }

    private StudentResultResponseDto buildResult(Exams exam,
                                                 StudentEnrollment enrollment,
                                                 List<ExamSubjects> examSubjects,
                                                 List<Marks> marks) {

        Map<UUID, Marks> marksBySubject = marks.stream()
                .filter(mark -> mark.getExamSubject() != null)
                .collect(Collectors.toMap(
                        mark -> mark.getExamSubject().getExamSubjectId(),
                        Function.identity(),
                        (first, second) -> first));

        List<StudentResultSubjectDto> rows = new ArrayList<>(examSubjects.size());
        BigDecimal totalObtained = BigDecimal.ZERO;
        int totalMax = 0;
        int marked = 0;

        for (ExamSubjects examSubject : examSubjects) {
            Marks mark = marksBySubject.get(examSubject.getExamSubjectId());
            BigDecimal obtained = mark == null ? null : mark.getMarksObtained();
            Integer maxMarks = examSubject.getMaxMarks();

            if (obtained != null) {
                marked++;
                totalObtained = totalObtained.add(obtained);
                totalMax += maxMarks == null ? 0 : maxMarks;
            }

            rows.add(StudentResultSubjectDto.builder()
                    .examSubjectId(examSubject.getExamSubjectId())
                    .subject(summaryMapper.toSubjectSummary(examSubject.getSubject()))
                    .maxMarks(maxMarks)
                    .marksObtained(obtained)
                    .percentage(percentage(obtained, maxMarks))
                    .build());
        }

        return StudentResultResponseDto.builder()
                .exam(examMapper.toSummary(exam))
                .student(summaryMapper.toStudentSummary(enrollment))
                .section(summaryMapper.toSectionSummary(enrollment.getSection()))
                .subjects(rows)
                .totalMarksObtained(totalObtained)
                .totalMaxMarks(totalMax)
                .percentage(percentage(totalObtained, totalMax))
                .subjectsMarked(marked)
                .subjectsTotal(examSubjects.size())
                .build();
    }

    /** Exam subjects that apply to the class the student is enrolled in. */
    private List<ExamSubjects> examSubjectsForEnrollment(UUID examId, StudentEnrollment enrollment) {
        Sections section = enrollment.getSection();
        if (section == null || section.getSchoolClass() == null) {
            return List.of();
        }
        return examSubjectsRepository.findByExamExamIdAndSchoolClassClassId(
                examId, section.getSchoolClass().getClassId());
    }

    private List<StudentEnrollment> roster(UUID sectionId) {
        return studentEnrollmentRepository
                .findBySectionSectionIdAndStatusOrderByRollNumberAsc(sectionId, RecordStatus.ACTIVE);
    }

    private BigDecimal percentage(BigDecimal obtained, Integer maxMarks) {
        if (obtained == null || maxMarks == null || maxMarks == 0) {
            return null;
        }
        return obtained.multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(maxMarks), PERCENTAGE_SCALE, RoundingMode.HALF_UP);
    }

    // ------------------------------------------------------------------
    // Validation
    // ------------------------------------------------------------------

    private void validateMarksWithinMax(BigDecimal marksObtained, ExamSubjects examSubject) {
        if (marksObtained == null) {
            return;
        }
        Integer maxMarks = examSubject.getMaxMarks();
        if (maxMarks != null && marksObtained.compareTo(BigDecimal.valueOf(maxMarks)) > 0) {
            throw new InvalidRequestException(
                    "Marks obtained (" + marksObtained + ") cannot exceed the maximum marks ("
                            + maxMarks + ") for this exam subject.");
        }
    }

    /** Marks are frozen once the exam is published. */
    private void validateExamOpenForMarks(ExamSubjects examSubject) {
        if (examSubject != null
                && examSubject.getExam() != null
                && examSubject.getExam().getStatus() == ExamStatus.PUBLISHED) {
            throw new BusinessRuleViolationException(
                    "Marks of a published exam cannot be added, changed or removed.");
        }
    }

    /** Stops a student being marked against a subject belonging to another class. */
    private void validateEnrollmentMatchesExamSubject(StudentEnrollment enrollment,
                                                      ExamSubjects examSubject) {
        UUID examClassId = examSubject.getSchoolClass() == null
                ? null : examSubject.getSchoolClass().getClassId();
        Sections section = enrollment.getSection();
        UUID studentClassId = section == null || section.getSchoolClass() == null
                ? null : section.getSchoolClass().getClassId();

        if (examClassId != null && studentClassId != null && !examClassId.equals(studentClassId)) {
            throw new InvalidRequestException(
                    "The student is not enrolled in the class this exam subject belongs to.");
        }
    }

    private void validateSectionMatchesExamSubject(Sections section, ExamSubjects examSubject) {
        UUID examClassId = examSubject.getSchoolClass() == null
                ? null : examSubject.getSchoolClass().getClassId();
        UUID sectionClassId = section.getSchoolClass() == null
                ? null : section.getSchoolClass().getClassId();

        if (examClassId != null && sectionClassId != null && !examClassId.equals(sectionClassId)) {
            throw new InvalidRequestException(
                    "The section does not belong to the class this exam subject belongs to.");
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

    // ------------------------------------------------------------------
    // Lookups
    // ------------------------------------------------------------------

    private Exams findExamOrThrow(UUID examId) {
        return examsRepository.findByExamId(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with id: " + examId));
    }

    private ExamSubjects findExamSubjectOrThrow(UUID examSubjectId) {
        return examSubjectsRepository.findByExamSubjectId(examSubjectId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Exam subject not found with id: " + examSubjectId));
    }

    private StudentEnrollment findEnrollmentOrThrow(UUID enrollmentId) {
        return studentEnrollmentRepository.findByEnrollmentId(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student enrollment not found with id: " + enrollmentId));
    }

    private Sections findSectionOrThrow(UUID sectionId) {
        return sectionsRepository.findBySectionId(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Section not found with id: " + sectionId));
    }

    private Teachers findTeacherOrThrow(UUID teacherId) {
        return teachersRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Teacher not found with id: " + teacherId));
    }
}
