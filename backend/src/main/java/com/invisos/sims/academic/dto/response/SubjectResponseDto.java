package com.invisos.sims.academic.dto.response;

import com.invisos.sims.common.enums.SubjectType;
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
public class SubjectResponseDto {

    private UUID subjectId;

    private String subjectName;

    private String subjectCode;

    private SubjectType subjectType;

    private Instant createdAt;

    private Instant updatedAt;
}
