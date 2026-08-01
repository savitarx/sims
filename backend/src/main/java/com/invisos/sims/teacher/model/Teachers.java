package com.invisos.sims.teacher.model;

import com.invisos.sims.common.entity.BaseEntity;

import com.invisos.sims.admin.model.AdminStaff;
import com.invisos.sims.auth.model.Users;
import com.invisos.sims.common.enums.TeacherDesignation;
import com.invisos.sims.common.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teachers extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "teacher_id", updatable = false, nullable = false)
    private UUID teacherId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "employee_id", unique = true,nullable = false)
    private String employeeId;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "parent_name",nullable = false)
    private String parentName;

    @Column(name = "contact",nullable = false)
    private String contact;


    @Column(name = "qualification",nullable = false)
    private String qualification;

    @Column(name = "photo_key")
    private String photoKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "designation",nullable=false)
    private TeacherDesignation designation;

    @Column(name = "joining_date",nullable = false)
    private LocalDate joiningDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status = UserStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private AdminStaff createdBy;
}
