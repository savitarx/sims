package com.invisos.sims.exam.model;

import com.invisos.sims.common.entity.BaseEntity;

import com.invisos.sims.student.model.StudentEnrollment;
import com.invisos.sims.teacher.model.Teachers;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(
        name = "marks",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_marks_enrollment_exam_subject",
                columnNames = {"enrollment_id", "exam_subject_id"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Marks extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "mark_id", updatable = false, nullable = false)
    private UUID markId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id")
    private StudentEnrollment enrollment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_subject_id")
    private ExamSubjects examSubject;

    @Column(name = "marks_obtained")
    private BigDecimal marksObtained;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entered_by")
    private Teachers enteredBy;
}
