package com.invisos.sims.exam.model;

import com.invisos.sims.academic.model.AcademicYears;
import com.invisos.sims.admin.model.AdminStaff;
import com.invisos.sims.common.entity.BaseEntity;
import com.invisos.sims.common.enums.ExamStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "exams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exams extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "exam_id", nullable = false, updatable = false)
    private UUID examId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id")
    private AcademicYears academicYear;

    @Column(name = "exam_name")
    private String examName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ExamStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private AdminStaff createdBy;
}
