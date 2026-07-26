-- V2: seed/sample data for local development.
-- Fixed UUIDs are used so the rows are easy to reference across tables.
-- password_hash values are placeholder BCrypt hashes (not real credentials).

-- ----------------------------------------------------------------------------
-- Users (1 admin, 3 teachers, 4 students)
-- ----------------------------------------------------------------------------
INSERT INTO users (user_id, login_id, password_hash, email, role, status, created_at, updated_at) VALUES
('33333333-3333-3333-3333-333333330001', 'admin',    '$2a$10$e0MYzXyjpJS7Pd0RVvHwHe1HlCS4bZJ18JuywdBLuF.j7YvvUn6bC', 'admin@sims.local',    'ADMIN',   'ACTIVE', NOW(6), NOW(6)),
('33333333-3333-3333-3333-333333330011', 'teacher1', '$2a$10$e0MYzXyjpJS7Pd0RVvHwHe1HlCS4bZJ18JuywdBLuF.j7YvvUn6bC', 'teacher1@sims.local', 'TEACHER', 'ACTIVE', NOW(6), NOW(6)),
('33333333-3333-3333-3333-333333330012', 'teacher2', '$2a$10$e0MYzXyjpJS7Pd0RVvHwHe1HlCS4bZJ18JuywdBLuF.j7YvvUn6bC', 'teacher2@sims.local', 'TEACHER', 'ACTIVE', NOW(6), NOW(6)),
('33333333-3333-3333-3333-333333330013', 'teacher3', '$2a$10$e0MYzXyjpJS7Pd0RVvHwHe1HlCS4bZJ18JuywdBLuF.j7YvvUn6bC', 'teacher3@sims.local', 'TEACHER', 'ACTIVE', NOW(6), NOW(6)),
('33333333-3333-3333-3333-333333330021', 'student1', '$2a$10$e0MYzXyjpJS7Pd0RVvHwHe1HlCS4bZJ18JuywdBLuF.j7YvvUn6bC', 'student1@sims.local', 'STUDENT', 'ACTIVE', NOW(6), NOW(6)),
('33333333-3333-3333-3333-333333330022', 'student2', '$2a$10$e0MYzXyjpJS7Pd0RVvHwHe1HlCS4bZJ18JuywdBLuF.j7YvvUn6bC', 'student2@sims.local', 'STUDENT', 'ACTIVE', NOW(6), NOW(6)),
('33333333-3333-3333-3333-333333330023', 'student3', '$2a$10$e0MYzXyjpJS7Pd0RVvHwHe1HlCS4bZJ18JuywdBLuF.j7YvvUn6bC', 'student3@sims.local', 'STUDENT', 'ACTIVE', NOW(6), NOW(6)),
('33333333-3333-3333-3333-333333330024', 'student4', '$2a$10$e0MYzXyjpJS7Pd0RVvHwHe1HlCS4bZJ18JuywdBLuF.j7YvvUn6bC', 'student4@sims.local', 'STUDENT', 'ACTIVE', NOW(6), NOW(6));

-- ----------------------------------------------------------------------------
-- Academic year (ACTIVE)
-- ----------------------------------------------------------------------------
INSERT INTO academic_years (academic_year_id, year_label, start_date, end_date, status, created_at, updated_at) VALUES
('11111111-1111-1111-1111-111111110001', '2025-2026', '2025-06-01', '2026-04-30', 'ACTIVE', NOW(6), NOW(6));

-- ----------------------------------------------------------------------------
-- Classes
-- ----------------------------------------------------------------------------
INSERT INTO classes (class_id, class_name, created_at, updated_at) VALUES
('22222222-2222-2222-2222-222222220001', 'Grade 1', NOW(6), NOW(6)),
('22222222-2222-2222-2222-222222220002', 'Grade 2', NOW(6), NOW(6));

-- ----------------------------------------------------------------------------
-- Admin staff (created_by is self-referencing and left NULL for the root admin)
-- ----------------------------------------------------------------------------
INSERT INTO admin_staff (admin_id, user_id, name, contact, designation, status, created_by, created_at, updated_at) VALUES
('44444444-4444-4444-4444-444444440001', '33333333-3333-3333-3333-333333330001', 'System Administrator', '9000000000', 'ADMIN', 'ACTIVE', NULL, NOW(6), NOW(6));

-- ----------------------------------------------------------------------------
-- Teachers
-- ----------------------------------------------------------------------------
INSERT INTO teachers (teacher_id, user_id, employee_id, name, contact, qualification, photo_url, designation, status, created_by, created_at, updated_at) VALUES
('55555555-5555-5555-5555-555555550001', '33333333-3333-3333-3333-333333330011', 'EMP001', 'Anita Rao',    '9000000011', 'M.Sc., B.Ed', NULL, 'SENIOR_TEACHER',    'ACTIVE', '44444444-4444-4444-4444-444444440001', NOW(6), NOW(6)),
('55555555-5555-5555-5555-555555550002', '33333333-3333-3333-3333-333333330012', 'EMP002', 'Bhaskar Nair', '9000000012', 'M.A., B.Ed',  NULL, 'ASSISTANT_TEACHER', 'ACTIVE', '44444444-4444-4444-4444-444444440001', NOW(6), NOW(6)),
('55555555-5555-5555-5555-555555550003', '33333333-3333-3333-3333-333333330013', 'EMP003', 'Chitra Menon', '9000000013', 'B.Sc., B.Ed', NULL, 'ASSISTANT_TEACHER', 'ACTIVE', '44444444-4444-4444-4444-444444440001', NOW(6), NOW(6));

-- ----------------------------------------------------------------------------
-- Students
-- ----------------------------------------------------------------------------
INSERT INTO students (student_id, user_id, admission_number, name, dob, gender, blood_group, photo_url, address, contact, emergency_contact, status, created_by, created_at, updated_at) VALUES
('66666666-6666-6666-6666-666666660001', '33333333-3333-3333-3333-333333330021', 'ADM001', 'Dev Kumar',    '2018-03-12', 'MALE',   'O+',  NULL, '12 Park Street',  '9000000021', '9000000031', 'ACTIVE', '44444444-4444-4444-4444-444444440001', NOW(6), NOW(6)),
('66666666-6666-6666-6666-666666660002', '33333333-3333-3333-3333-333333330022', 'ADM002', 'Esha Pillai',  '2018-07-24', 'FEMALE', 'A+',  NULL, '9 Lake View',     '9000000022', '9000000032', 'ACTIVE', '44444444-4444-4444-4444-444444440001', NOW(6), NOW(6)),
('66666666-6666-6666-6666-666666660003', '33333333-3333-3333-3333-333333330023', 'ADM003', 'Farhan Ali',   '2017-11-05', 'MALE',   'B+',  NULL, '5 Hill Road',     '9000000023', '9000000033', 'ACTIVE', '44444444-4444-4444-4444-444444440001', NOW(6), NOW(6)),
('66666666-6666-6666-6666-666666660004', '33333333-3333-3333-3333-333333330024', 'ADM004', 'Gita Sharma',  '2017-09-18', 'FEMALE', 'AB+', NULL, '20 Rose Lane',    '9000000024', '9000000034', 'ACTIVE', '44444444-4444-4444-4444-444444440001', NOW(6), NOW(6));

-- ----------------------------------------------------------------------------
-- Sections (2 per class); class_teacher assigned, assigned_by = admin
-- ----------------------------------------------------------------------------
INSERT INTO sections (section_id, class_id, academic_year_id, section_name, class_teacher_id, assigned_by, created_at, updated_at) VALUES
('77777777-7777-7777-7777-777777770001', '22222222-2222-2222-2222-222222220001', '11111111-1111-1111-1111-111111110001', 'A', '55555555-5555-5555-5555-555555550001', '44444444-4444-4444-4444-444444440001', NOW(6), NOW(6)),
('77777777-7777-7777-7777-777777770002', '22222222-2222-2222-2222-222222220001', '11111111-1111-1111-1111-111111110001', 'B', '55555555-5555-5555-5555-555555550002', '44444444-4444-4444-4444-444444440001', NOW(6), NOW(6)),
('77777777-7777-7777-7777-777777770003', '22222222-2222-2222-2222-222222220002', '11111111-1111-1111-1111-111111110001', 'A', '55555555-5555-5555-5555-555555550003', '44444444-4444-4444-4444-444444440001', NOW(6), NOW(6)),
('77777777-7777-7777-7777-777777770004', '22222222-2222-2222-2222-222222220002', '11111111-1111-1111-1111-111111110001', 'B', NULL,                                   '44444444-4444-4444-4444-444444440001', NOW(6), NOW(6));

-- ----------------------------------------------------------------------------
-- Student enrollments (unique per section+roll_number)
-- ----------------------------------------------------------------------------
INSERT INTO student_enrollment (enrollment_id, student_id, academic_year_id, section_id, roll_number, status, created_at, updated_at) VALUES
('88888888-8888-8888-8888-888888880001', '66666666-6666-6666-6666-666666660001', '11111111-1111-1111-1111-111111110001', '77777777-7777-7777-7777-777777770001', '1', 'ACTIVE', NOW(6), NOW(6)),
('88888888-8888-8888-8888-888888880002', '66666666-6666-6666-6666-666666660002', '11111111-1111-1111-1111-111111110001', '77777777-7777-7777-7777-777777770001', '2', 'ACTIVE', NOW(6), NOW(6)),
('88888888-8888-8888-8888-888888880003', '66666666-6666-6666-6666-666666660003', '11111111-1111-1111-1111-111111110001', '77777777-7777-7777-7777-777777770002', '1', 'ACTIVE', NOW(6), NOW(6)),
('88888888-8888-8888-8888-888888880004', '66666666-6666-6666-6666-666666660004', '11111111-1111-1111-1111-111111110001', '77777777-7777-7777-7777-777777770003', '1', 'ACTIVE', NOW(6), NOW(6));
