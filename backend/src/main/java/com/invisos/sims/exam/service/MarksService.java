package com.invisos.sims.exam.service;

import com.invisos.sims.exam.dto.request.MarkRequestDto;
import com.invisos.sims.exam.dto.request.MarksSheetSaveRequestDto;
import com.invisos.sims.exam.dto.response.MarksSheetResponseDto;
import com.invisos.sims.exam.dto.response.StudentResultResponseDto;
import com.invisos.sims.exam.model.Marks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface MarksService {

    Page<Marks> findAll(Pageable pageable);

    Marks findById(UUID id);

    Page<Marks> findByExamSubjectId(UUID examSubjectId, Pageable pageable);

    List<Marks> findByEnrollmentId(UUID enrollmentId);

    Marks create(MarkRequestDto request);

    Marks update(UUID id, MarkRequestDto request);

    void delete(UUID id);

    /** Marks-entry screen: roster for one subject and section with marks pre-filled. */
    MarksSheetResponseDto getMarksSheet(UUID examSubjectId, UUID sectionId);

    /** Transactional bulk upsert of a whole marks sheet. */
    MarksSheetResponseDto saveMarksSheet(MarksSheetSaveRequestDto request);

    /** One student's result across every subject of an exam. */
    StudentResultResponseDto getStudentResult(UUID examId, UUID enrollmentId);

    /** Result summary for an entire section. */
    List<StudentResultResponseDto> getSectionResults(UUID examId, UUID sectionId);
}
