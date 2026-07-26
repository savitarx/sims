package com.invisos.sims.fee.model;

import com.invisos.sims.common.enums.FeeStatus;
import com.invisos.sims.student.model.StudentEnrollment;
import com.invisos.sims.teacher.model.Teachers;
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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(
        name = "student_fee_status",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_student_fee_enrollment_fee",
                columnNames = {"enrollment_id", "fee_id"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentFeeStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "student_fee_status_id", updatable = false, nullable = false)
    private UUID studentFeeStatusId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id")
    private StudentEnrollment enrollment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_id")
    private Fees fee;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FeeStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private Teachers updatedBy;
}
