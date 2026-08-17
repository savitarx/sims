package com.invisos.sims.academic.dto.response;

import com.invisos.sims.common.enums.UserStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionResponseDto {
    private UUID sectionId;

    private UUID classId;
    private String className;

    private UUID academicYearId;
    private String academicYearName;

    private String sectionName;

    private UUID classTeacherId;
    private String classTeacherName;
    private UserStatus classTeacherStatus;

//    private UUID assignedById;
}
