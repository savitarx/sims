package com.invisos.sims.common.dto.summary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * A student as seen through an enrollment: the enrollment is what exam and fee
 * records point at, while the name and admission number come from the student.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentSummaryDto {

    private UUID enrollmentId;

    private UUID studentId;

    private String name;

    private String rollNumber;

    private String admissionNumber;
}
