package com.invisos.sims.student.dto;

import com.invisos.sims.common.enums.UserStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Inbound payload for creating/updating a Student. Association targets are
 * referenced by id (userId, createdById); the service layer is responsible for
 * resolving them into entities.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRequestDto {

    private UUID userId;

    @NotBlank
    private String admissionNumber;

    @NotBlank
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
}
