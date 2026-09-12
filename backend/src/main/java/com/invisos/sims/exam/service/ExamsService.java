package com.invisos.sims.exam.service;

import com.invisos.sims.common.enums.ExamStatus;
import com.invisos.sims.exam.dto.request.ExamRequestDto;
import com.invisos.sims.exam.dto.response.ExamOverviewResponseDto;
import com.invisos.sims.exam.dto.response.PublishPreviewResponseDto;
import com.invisos.sims.exam.model.Exams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ExamsService {

    Page<Exams> findAll(UUID academicYearId, ExamStatus status, Pageable pageable);

    Exams findById(UUID id);

    Exams create(ExamRequestDto request);

    Exams update(UUID id, ExamRequestDto request);

    Exams publish(UUID id);

    void delete(UUID id);

    /** Exam dashboard: scheduling coverage and marks-entry progress per subject. */
    ExamOverviewResponseDto getOverview(UUID examId);

    /** Publish readiness as a checklist, without attempting the publish. */
    PublishPreviewResponseDto getPublishPreview(UUID examId);
}
