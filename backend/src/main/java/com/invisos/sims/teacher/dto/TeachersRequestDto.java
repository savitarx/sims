package com.invisos.sims.teacher.dto;

import com.invisos.sims.common.enums.TeacherDesignation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeachersRequestDto {

//   private UsersDto usersDto;

    @NotBlank(message = "Please enter the Employee id")
    private String employeeId;

    @NotBlank(message = "Please enter the Name of the Teacher")
    private String name;

    @NotBlank(message = "Please enter the Parent name")
    private String parentName;


    @NotBlank(message = "Please enter the email of Teacher")
    @Email
    private String email;

    @NotBlank(message = "Please enter the contact number of the teacher")
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be 10 digits")
    private String contact;

    @NotBlank(message = "Please enter the qualification of the Teacher")
    private String qualification;

    @NotNull(message = "Please enter the Teacher's joining Date")
    private LocalDate joiningDate;

    private String photoKey;

    @NotNull(message = "Please enter the Teacher Designation")
    private TeacherDesignation designation;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TeachersRequestDto that = (TeachersRequestDto) o;
        return Objects.equals(employeeId, that.employeeId) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, email);
    }
}
