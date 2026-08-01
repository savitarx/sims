package com.invisos.sims.teacher.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeachersCountResponseDto {

    private long totalTeachers;
    private long activeTeachers;
    private long inactiveTeachers;
}