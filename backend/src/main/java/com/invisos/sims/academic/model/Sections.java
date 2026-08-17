package com.invisos.sims.academic.model;

import com.invisos.sims.common.entity.BaseEntity;

import com.invisos.sims.admin.model.AdminStaff;
import com.invisos.sims.teacher.model.Teachers;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(
        name = "sections",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_sections_class_year_name",
                        columnNames = {
                                "class_id",
                                "academic_year_id",
                                "section_name"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sections extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "section_id", updatable = false, nullable = false)
    private UUID sectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private Classes schoolClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYears academicYear;

    @Column(name = "section_name", nullable = false)
    private String sectionName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_teacher_id")
    private Teachers classTeacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by")
    private AdminStaff assignedBy;
}