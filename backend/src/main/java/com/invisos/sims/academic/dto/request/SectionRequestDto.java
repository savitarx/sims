package com.invisos.sims.academic.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionRequestDto {

    @NotNull
    private UUID classId;

    @NotNull
    private UUID academicYearId;

    @NotBlank
    private String sectionName;

    private UUID classTeacherId;
}