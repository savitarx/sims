package com.invisos.sims.exam.repository.projection;

import java.util.UUID;

/**
 * Grouped marks count per exam subject, so the exam overview can report entry
 * progress for every subject in one query instead of one query per subject.
 */
public interface ExamSubjectMarkCount {

    UUID getExamSubjectId();

    long getTotal();
}
