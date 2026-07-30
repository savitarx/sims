package com.invisos.sims.exam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamSubjectResponseDto {

    private UUID examSubjectId;

    private UUID examId;

    private UUID subjectId;

    private UUID classId;

    private Integer maxMarks;

    private Instant createdAt;

    private Instant updatedAt;
}
