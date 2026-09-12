package com.invisos.sims.common.dto.summary;

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
public class SectionSummaryDto {

    private UUID id;

    /** Section name only, e.g. "A". */
    private String name;

    private UUID classId;

    private String className;

    /** Convenience label for headings, e.g. "Grade 10 - A". */
    private String displayName;
}
