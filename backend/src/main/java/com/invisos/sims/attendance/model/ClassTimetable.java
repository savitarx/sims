package com.invisos.sims.attendance.model;

import com.invisos.sims.academic.model.Sections;
import com.invisos.sims.academic.model.Subjects;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "class_timetable")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassTimetable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "timetable_id", updatable = false, nullable = false)
    private UUID timetableId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Sections section;

    @Column(name = "day")
    private String day;

    @Column(name = "period_number")
    private Integer periodNumber;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subjects subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teachers teacher;
}
