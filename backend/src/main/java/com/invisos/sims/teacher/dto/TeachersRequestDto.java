package com.invisos.sims.teacher.dto;

import com.invisos.sims.common.enums.TeacherDesignation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeachersRequestDto {

//   private UsersDto usersDto;

    @NotBlank(message = "Please enter the name")
    private String employeeId;

    @NotBlank(message = "Please enter the Name")
    private String name;

    @NotBlank(message = "Please enter the Parent name")
    private String parentName;


    @NotBlank(message = "Please enter the email")
    @Email
    private String email;

    @NotBlank(message = "Please enter the contact number")
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be 10 digits")
    private String contact;

    @NotBlank(message = "Please enter the qualification")
    private String qualification;

    @NotBlank(message = "Please enter the joining Date")
    private LocalDate joiningDate;

    private String photoUrl;

    @NotNull(message = "Please enter the Designation")
    private TeacherDesignation designation;


}
