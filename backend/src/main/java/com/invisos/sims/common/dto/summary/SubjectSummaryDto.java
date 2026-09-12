package com.invisos.sims.common.dto.summary;

import com.invisos.sims.common.enums.SubjectType;
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
public class SubjectSummaryDto {

    private UUID id;

    private String name;

    private String code;

    private SubjectType type;
}
