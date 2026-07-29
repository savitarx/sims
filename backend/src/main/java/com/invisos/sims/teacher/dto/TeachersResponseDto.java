package com.invisos.sims.teacher.dto;

import com.invisos.sims.common.enums.TeacherDesignation;
import com.invisos.sims.common.enums.UserRole;
import com.invisos.sims.common.enums.UserStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeachersResponseDto {

        private UUID teacherId;

        private UUID userId;

        private UserRole role;

        private String employeeId;

        private String name;

        private String email;

        private String contact;

        private LocalDate joiningDate;

        private String qualification;

        private String photoUrl;

        private TeacherDesignation designation;

        private UserStatus status;

    }


