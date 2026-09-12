-- V3: audit (`updated_by`) columns and supporting indexes for the exam, fee and
-- communication modules.
--
-- Actor type follows the table's existing convention: admin-managed records point
-- at admin_staff, teacher-managed records point at teachers.
-- All columns are nullable so existing rows remain valid.

-- ----------------------------------------------------------------------------
-- exam
-- ----------------------------------------------------------------------------
ALTER TABLE exams
    ADD COLUMN updated_by CHAR(36) NULL AFTER created_by,
    ADD CONSTRAINT fk_exams_updated_by FOREIGN KEY (updated_by) REFERENCES admin_staff (admin_id);

ALTER TABLE exam_subjects
    ADD COLUMN updated_by CHAR(36) NULL,
    ADD CONSTRAINT fk_exam_subjects_updated_by FOREIGN KEY (updated_by) REFERENCES admin_staff (admin_id);

ALTER TABLE exam_timetable
    ADD COLUMN updated_by CHAR(36) NULL,
    ADD CONSTRAINT fk_exam_timetable_updated_by FOREIGN KEY (updated_by) REFERENCES admin_staff (admin_id);

-- Marks are entered and corrected by teachers.
ALTER TABLE marks
    ADD COLUMN updated_by CHAR(36) NULL AFTER entered_by,
    ADD CONSTRAINT fk_marks_updated_by FOREIGN KEY (updated_by) REFERENCES teachers (teacher_id);

-- ----------------------------------------------------------------------------
-- fee
-- ----------------------------------------------------------------------------
ALTER TABLE fees
    ADD COLUMN updated_by CHAR(36) NULL,
    ADD CONSTRAINT fk_fees_updated_by FOREIGN KEY (updated_by) REFERENCES admin_staff (admin_id);

-- student_fee_status.updated_by already references teachers. A fee status may
-- also be changed by admin staff, so a parallel nullable admin column is added;
-- the service enforces that exactly one of the two is set.
ALTER TABLE student_fee_status
    ADD COLUMN updated_by_admin CHAR(36) NULL AFTER updated_by,
    ADD CONSTRAINT fk_student_fee_updated_by_admin FOREIGN KEY (updated_by_admin) REFERENCES admin_staff (admin_id);

-- ----------------------------------------------------------------------------
-- communication
-- ----------------------------------------------------------------------------
ALTER TABLE announcements
    ADD COLUMN updated_by CHAR(36) NULL AFTER created_by,
    ADD CONSTRAINT fk_announcements_updated_by FOREIGN KEY (updated_by) REFERENCES admin_staff (admin_id);

ALTER TABLE events
    ADD COLUMN updated_by CHAR(36) NULL AFTER created_by,
    ADD CONSTRAINT fk_events_updated_by FOREIGN KEY (updated_by) REFERENCES admin_staff (admin_id);

-- ----------------------------------------------------------------------------
-- indexes for the new business queries
-- InnoDB already indexes every FK column, so only non-FK access paths are added.
-- ----------------------------------------------------------------------------

-- Announcement feed: ordered by recency, optionally filtered by priority.
CREATE INDEX idx_announcements_created_at ON announcements (created_at);
CREATE INDEX idx_announcements_priority_created_at ON announcements (priority, created_at);

-- Calendar / upcoming events: date-range scans.
CREATE INDEX idx_events_start_date ON events (start_date);
CREATE INDEX idx_events_date_range ON events (start_date, end_date);

-- Exam timetable is read date-first when building an exam schedule.
CREATE INDEX idx_exam_timetable_exam_date ON exam_timetable (exam_date);

-- One timetable slot per exam subject (previously enforced only in the service).
CREATE UNIQUE INDEX uk_exam_timetable_exam_subject ON exam_timetable (exam_subject_id);
