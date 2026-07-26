package com.invisos.sims.student.dto;

import com.invisos.sims.common.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Outbound representation of a Student. Associations are flattened to their ids.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponseDto {

    private UUID studentId;
    private UUID userId;
    private String admissionNumber;
    private String name;
    private LocalDate dob;
    private String gender;
    private String bloodGroup;
    private String photoUrl;
    private String address;
    private String contact;
    private String emergencyContact;
    private UserStatus status;
    private UUID createdById;
    private Instant createdAt;
    private Instant updatedAt;
}
