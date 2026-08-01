-- V1: initial schema for the School Management System.
-- Column names/types mirror the JPA entities. UUID PK/FK columns are CHAR(36)
-- (see spring.jpa.properties.hibernate.type.preferred_uuid_jdbc_type=CHAR).
-- created_at/updated_at come from BaseEntity and exist on every table.

-- ----------------------------------------------------------------------------
-- auth
-- ----------------------------------------------------------------------------
CREATE TABLE users (
    user_id       CHAR(36) NOT NULL,
    login_id      VARCHAR(255),
    password_hash VARCHAR(255),
    email         VARCHAR(255),
    role          VARCHAR(50),
    status        VARCHAR(50),
    last_login    DATETIME(6),
    created_at    DATETIME(6),
    updated_at    DATETIME(6),
    PRIMARY KEY (user_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------------------------
-- academic (no FKs)
-- ----------------------------------------------------------------------------
CREATE TABLE academic_years (
    academic_year_id CHAR(36) NOT NULL,
    year_label       VARCHAR(255),
    start_date       DATE,
    end_date         DATE,
    status           VARCHAR(50),
    created_at       DATETIME(6),
    updated_at       DATETIME(6),
    PRIMARY KEY (academic_year_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE classes (
    class_id   CHAR(36) NOT NULL,
    class_name VARCHAR(255),
    created_at DATETIME(6),
    updated_at DATETIME(6),
    PRIMARY KEY (class_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE subjects (
    subject_id   CHAR(36) NOT NULL,
    subject_name VARCHAR(255),
    subject_code VARCHAR(255),
    subject_type VARCHAR(50),
    created_at   DATETIME(6),
    updated_at   DATETIME(6),
    PRIMARY KEY (subject_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------------------------
-- admin (FK -> users, self)
-- ----------------------------------------------------------------------------
CREATE TABLE admin_staff (
    admin_id    CHAR(36) NOT NULL,
    user_id     CHAR(36),
    name        VARCHAR(255),
    contact     VARCHAR(255),
    designation VARCHAR(50),
    status      VARCHAR(50),
    created_by  CHAR(36),
    created_at  DATETIME(6),
    updated_at  DATETIME(6),
    PRIMARY KEY (admin_id),
    CONSTRAINT fk_admin_staff_user FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT fk_admin_staff_created_by FOREIGN KEY (created_by) REFERENCES admin_staff (admin_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------------------------
-- teacher (FK -> users, admin_staff)
-- ----------------------------------------------------------------------------
CREATE TABLE teachers (
    teacher_id    CHAR(36) NOT NULL,
    user_id       CHAR(36),
    employee_id   VARCHAR(255),
    name          VARCHAR(255),
    parent_name   VARCHAR(255),
    contact       VARCHAR(255),
    joining_date  DATE,
    qualification VARCHAR(255),
    photo_key     VARCHAR(255),
    designation   VARCHAR(50),
    status        VARCHAR(50),
    created_by    CHAR(36),
    created_at    DATETIME(6),
    updated_at    DATETIME(6),
    PRIMARY KEY (teacher_id),
    CONSTRAINT uk_teachers_employee_id UNIQUE (employee_id),
    CONSTRAINT fk_teachers_user FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT fk_teachers_created_by FOREIGN KEY (created_by) REFERENCES admin_staff (admin_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------------------------
-- student (FK -> users, admin_staff)
-- ----------------------------------------------------------------------------
CREATE TABLE students (
    student_id        CHAR(36) NOT NULL,
    user_id           CHAR(36),
    admission_number  VARCHAR(255),
    name              VARCHAR(255),
    dob               DATE,
    gender            VARCHAR(255),
    blood_group       VARCHAR(255),
    photo_url         VARCHAR(255),
    address           VARCHAR(255),
    contact           VARCHAR(255),
    emergency_contact VARCHAR(255),
    status            VARCHAR(50),
    created_by        CHAR(36),
    created_at        DATETIME(6),
    updated_at        DATETIME(6),
    PRIMARY KEY (student_id),
    CONSTRAINT uk_students_admission_number UNIQUE (admission_number),
    CONSTRAINT fk_students_user FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT fk_students_created_by FOREIGN KEY (created_by) REFERENCES admin_staff (admin_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------------------------
-- academic.sections (FK -> classes, academic_years, teachers, admin_staff)
-- ----------------------------------------------------------------------------
CREATE TABLE sections (
    section_id       CHAR(36) NOT NULL,
    class_id         CHAR(36),
    academic_year_id CHAR(36),
    section_name     VARCHAR(255),
    class_teacher_id CHAR(36),
    assigned_by      CHAR(36),
    created_at       DATETIME(6),
    updated_at       DATETIME(6),
    PRIMARY KEY (section_id),
    CONSTRAINT fk_sections_class FOREIGN KEY (class_id) REFERENCES classes (class_id),
    CONSTRAINT fk_sections_academic_year FOREIGN KEY (academic_year_id) REFERENCES academic_years (academic_year_id),
    CONSTRAINT fk_sections_class_teacher FOREIGN KEY (class_teacher_id) REFERENCES teachers (teacher_id),
    CONSTRAINT fk_sections_assigned_by FOREIGN KEY (assigned_by) REFERENCES admin_staff (admin_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------------------------
-- admin.principal_tenure (FK -> admin_staff, academic_years)
-- ----------------------------------------------------------------------------
CREATE TABLE principal_tenure (
    tenure_id        CHAR(36) NOT NULL,
    admin_id         CHAR(36),
    academic_year_id CHAR(36),
    start_date       DATE,
    end_date         DATE,
    created_at       DATETIME(6),
    updated_at       DATETIME(6),
    PRIMARY KEY (tenure_id),
    CONSTRAINT fk_principal_tenure_admin FOREIGN KEY (admin_id) REFERENCES admin_staff (admin_id),
    CONSTRAINT fk_principal_tenure_academic_year FOREIGN KEY (academic_year_id) REFERENCES academic_years (academic_year_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------------------------
-- student.parents (FK -> students)
-- ----------------------------------------------------------------------------
CREATE TABLE parents (
    parent_id     CHAR(36) NOT NULL,
    student_id    CHAR(36),
    name          VARCHAR(255),
    contact       VARCHAR(255),
    address       VARCHAR(255),
    relation_type VARCHAR(50),
    created_at    DATETIME(6),
    updated_at    DATETIME(6),
    PRIMARY KEY (parent_id),
    CONSTRAINT fk_parents_student FOREIGN KEY (student_id) REFERENCES students (student_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------------------------
-- student.student_enrollment (FK -> students, academic_years, sections)
-- ----------------------------------------------------------------------------
CREATE TABLE student_enrollment (
    enrollment_id    CHAR(36) NOT NULL,
    student_id       CHAR(36),
    academic_year_id CHAR(36),
    section_id       CHAR(36),
    roll_number      VARCHAR(255),
    status           VARCHAR(50),
    created_at       DATETIME(6),
    updated_at       DATETIME(6),
    PRIMARY KEY (enrollment_id),
    CONSTRAINT uk_enrollment_section_roll UNIQUE (section_id, roll_number),
    CONSTRAINT fk_enrollment_student FOREIGN KEY (student_id) REFERENCES students (student_id),
    CONSTRAINT fk_enrollment_academic_year FOREIGN KEY (academic_year_id) REFERENCES academic_years (academic_year_id),
    CONSTRAINT fk_enrollment_section FOREIGN KEY (section_id) REFERENCES sections (section_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------------------------
-- student.student_subjects (FK -> student_enrollment, subjects)
-- ----------------------------------------------------------------------------
CREATE TABLE student_subjects (
    student_subject_id CHAR(36) NOT NULL,
    enrollment_id      CHAR(36),
    subject_id         CHAR(36),
    created_at         DATETIME(6),
    updated_at         DATETIME(6),
    PRIMARY KEY (student_subject_id),
    CONSTRAINT fk_student_subjects_enrollment FOREIGN KEY (enrollment_id) REFERENCES student_enrollment (enrollment_id),
    CONSTRAINT fk_student_subjects_subject FOREIGN KEY (subject_id) REFERENCES subjects (subject_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------------------------
-- teacher.teacher_assignment (FK -> teachers, academic_years, sections, subjects, admin_staff)
-- ----------------------------------------------------------------------------
CREATE TABLE teacher_assignment (
    assignment_id    CHAR(36) NOT NULL,
    teacher_id       CHAR(36),
    academic_year_id CHAR(36),
    section_id       CHAR(36),
    subject_id       CHAR(36),
    assigned_by      CHAR(36),
    created_at       DATETIME(6),
    updated_at       DATETIME(6),
    PRIMARY KEY (assignment_id),
    CONSTRAINT uk_teacher_section_subject_year UNIQUE (teacher_id, section_id, subject_id, academic_year_id),
    CONSTRAINT fk_assignment_teacher FOREIGN KEY (teacher_id) REFERENCES teachers (teacher_id),
    CONSTRAINT fk_assignment_academic_year FOREIGN KEY (academic_year_id) REFERENCES academic_years (academic_year_id),
    CONSTRAINT fk_assignment_section FOREIGN KEY (section_id) REFERENCES sections (section_id),
    CONSTRAINT fk_assignment_subject FOREIGN KEY (subject_id) REFERENCES subjects (subject_id),
    CONSTRAINT fk_assignment_assigned_by FOREIGN KEY (assigned_by) REFERENCES admin_staff (admin_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------------------------
-- attendance.attendance (FK -> student_enrollment, teachers)
-- ----------------------------------------------------------------------------
CREATE TABLE attendance (
    attendance_id CHAR(36) NOT NULL,
    enrollment_id CHAR(36),
    att_date      DATE,
    status        VARCHAR(50),
    marked_by     CHAR(36),
    edited_by     CHAR(36),
    edited_at     DATETIME(6),
    created_at    DATETIME(6),
    updated_at    DATETIME(6),
    PRIMARY KEY (attendance_id),
    CONSTRAINT uk_attendance_enrollment_date UNIQUE (enrollment_id, att_date),
    CONSTRAINT fk_attendance_enrollment FOREIGN KEY (enrollment_id) REFERENCES student_enrollment (enrollment_id),
    CONSTRAINT fk_attendance_marked_by FOREIGN KEY (marked_by) REFERENCES teachers (teacher_id),
    CONSTRAINT fk_attendance_edited_by FOREIGN KEY (edited_by) REFERENCES teachers (teacher_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------------------------
-- attendance.class_timetable (FK -> sections, subjects, teachers)
-- ----------------------------------------------------------------------------
CREATE TABLE class_timetable (
    timetable_id  CHAR(36) NOT NULL,
    section_id    CHAR(36),
    `day`         VARCHAR(255),
    period_number INT,
    start_time    TIME(6),
    end_time      TIME(6),
    subject_id    CHAR(36),
    teacher_id    CHAR(36),
    created_at    DATETIME(6),
    updated_at    DATETIME(6),
    PRIMARY KEY (timetable_id),
    CONSTRAINT fk_timetable_section FOREIGN KEY (section_id) REFERENCES sections (section_id),
    CONSTRAINT fk_timetable_subject FOREIGN KEY (subject_id) REFERENCES subjects (subject_id),
    CONSTRAINT fk_timetable_teacher FOREIGN KEY (teacher_id) REFERENCES teachers (teacher_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------------------------
-- exam.exams (FK -> academic_years, admin_staff)
-- ----------------------------------------------------------------------------
CREATE TABLE exams (
    exam_id          CHAR(36) NOT NULL,
    academic_year_id CHAR(36),
    exam_name        VARCHAR(255),
    status           VARCHAR(50),
    created_by       CHAR(36),
    created_at       DATETIME(6),
    updated_at       DATETIME(6),
    PRIMARY KEY (exam_id),
    CONSTRAINT fk_exams_academic_year FOREIGN KEY (academic_year_id) REFERENCES academic_years (academic_year_id),
    CONSTRAINT fk_exams_created_by FOREIGN KEY (created_by) REFERENCES admin_staff (admin_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------------------------
-- exam.exam_subjects (FK -> exams, subjects, classes)
-- ----------------------------------------------------------------------------
CREATE TABLE exam_subjects (
    exam_subject_id CHAR(36) NOT NULL,
    exam_id         CHAR(36),
    subject_id      CHAR(36),
    class_id        CHAR(36),
    max_marks       INT,
    created_at      DATETIME(6),
    updated_at      DATETIME(6),
    PRIMARY KEY (exam_subject_id),
    CONSTRAINT fk_exam_subjects_exam FOREIGN KEY (exam_id) REFERENCES exams (exam_id),
    CONSTRAINT fk_exam_subjects_subject FOREIGN KEY (subject_id) REFERENCES subjects (subject_id),
    CONSTRAINT fk_exam_subjects_class FOREIGN KEY (class_id) REFERENCES classes (class_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------------------------
-- exam.exam_timetable (FK -> exam_subjects)
-- ----------------------------------------------------------------------------
CREATE TABLE exam_timetable (
    exam_timetable_id CHAR(36) NOT NULL,
    exam_subject_id   CHAR(36),
    exam_date         DATE,
    exam_time         TIME(6),
    created_at        DATETIME(6),
    updated_at        DATETIME(6),
    PRIMARY KEY (exam_timetable_id),
    CONSTRAINT fk_exam_timetable_exam_subject FOREIGN KEY (exam_subject_id) REFERENCES exam_subjects (exam_subject_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------------------------
-- exam.marks (FK -> student_enrollment, exam_subjects, teachers)
-- ----------------------------------------------------------------------------
CREATE TABLE marks (
    mark_id         CHAR(36) NOT NULL,
    enrollment_id   CHAR(36),
    exam_subject_id CHAR(36),
    marks_obtained  DECIMAL(6, 2),
    entered_by      CHAR(36),
    created_at      DATETIME(6),
    updated_at      DATETIME(6),
    PRIMARY KEY (mark_id),
    CONSTRAINT uk_marks_enrollment_exam_subject UNIQUE (enrollment_id, exam_subject_id),
    CONSTRAINT fk_marks_enrollment FOREIGN KEY (enrollment_id) REFERENCES student_enrollment (enrollment_id),
    CONSTRAINT fk_marks_exam_subject FOREIGN KEY (exam_subject_id) REFERENCES exam_subjects (exam_subject_id),
    CONSTRAINT fk_marks_entered_by FOREIGN KEY (entered_by) REFERENCES teachers (teacher_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------------------------
-- fee.fees (FK -> classes, academic_years)
-- ----------------------------------------------------------------------------
CREATE TABLE fees (
    fee_id           CHAR(36) NOT NULL,
    class_id         CHAR(36),
    academic_year_id CHAR(36),
    term_name        VARCHAR(255),
    created_at       DATETIME(6),
    updated_at       DATETIME(6),
    PRIMARY KEY (fee_id),
    CONSTRAINT fk_fees_class FOREIGN KEY (class_id) REFERENCES classes (class_id),
    CONSTRAINT fk_fees_academic_year FOREIGN KEY (academic_year_id) REFERENCES academic_years (academic_year_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------------------------
-- fee.student_fee_status (FK -> student_enrollment, fees, teachers)
-- ----------------------------------------------------------------------------
CREATE TABLE student_fee_status (
    student_fee_status_id CHAR(36) NOT NULL,
    enrollment_id         CHAR(36),
    fee_id                CHAR(36),
    status                VARCHAR(50),
    updated_by            CHAR(36),
    created_at            DATETIME(6),
    updated_at            DATETIME(6),
    PRIMARY KEY (student_fee_status_id),
    CONSTRAINT uk_student_fee_enrollment_fee UNIQUE (enrollment_id, fee_id),
    CONSTRAINT fk_student_fee_enrollment FOREIGN KEY (enrollment_id) REFERENCES student_enrollment (enrollment_id),
    CONSTRAINT fk_student_fee_fee FOREIGN KEY (fee_id) REFERENCES fees (fee_id),
    CONSTRAINT fk_student_fee_updated_by FOREIGN KEY (updated_by) REFERENCES teachers (teacher_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------------------------
-- communication.events (FK -> classes nullable, admin_staff)
-- ----------------------------------------------------------------------------
CREATE TABLE events (
    event_id    CHAR(36) NOT NULL,
    title       VARCHAR(255),
    description VARCHAR(255),
    start_date  DATE,
    end_date    DATE,
    class_id    CHAR(36),
    created_by  CHAR(36),
    created_at  DATETIME(6),
    updated_at  DATETIME(6),
    PRIMARY KEY (event_id),
    CONSTRAINT fk_events_class FOREIGN KEY (class_id) REFERENCES classes (class_id),
    CONSTRAINT fk_events_created_by FOREIGN KEY (created_by) REFERENCES admin_staff (admin_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------------------------
-- communication.announcements (FK -> admin_staff)
-- ----------------------------------------------------------------------------
CREATE TABLE announcements (
    announcement_id CHAR(36) NOT NULL,
    title           VARCHAR(255),
    message         VARCHAR(255),
    priority        VARCHAR(50),
    created_by      CHAR(36),
    created_at      DATETIME(6),
    updated_at      DATETIME(6),
    PRIMARY KEY (announcement_id),
    CONSTRAINT fk_announcements_created_by FOREIGN KEY (created_by) REFERENCES admin_staff (admin_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
