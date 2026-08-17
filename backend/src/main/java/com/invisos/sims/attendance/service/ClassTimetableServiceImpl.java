package com.invisos.sims.attendance.service;

import com.invisos.sims.academic.model.Sections;
import com.invisos.sims.academic.model.Subjects;
import com.invisos.sims.academic.service.SectionsService;
import com.invisos.sims.academic.service.SubjectsService;
import com.invisos.sims.attendance.dto.ClassTimeTableBulkRequestDto;
import com.invisos.sims.attendance.dto.ClassTimeTableRequestDto;
import com.invisos.sims.attendance.dto.ClassTimeTableResponseDto;
import com.invisos.sims.attendance.mapper.ClassTimeTableMapper;
import com.invisos.sims.attendance.model.ClassTimetable;
import com.invisos.sims.attendance.repository.ClassTimetableRepository;
import com.invisos.sims.common.exception.InvalidRequestException;
import com.invisos.sims.common.exception.ResourceAlreadyExistsException;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import com.invisos.sims.teacher.model.Teachers;
import com.invisos.sims.teacher.service.TeachersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class ClassTimetableServiceImpl implements ClassTimetableService {

    private final ClassTimetableRepository classTimetableRepository;

    private final ClassTimeTableMapper mapper;

    private final SectionsService sectionsService;

    private final SubjectsService subjectsService;

    private final TeachersService teachersService;


    public ClassTimetableServiceImpl(ClassTimetableRepository classTimetableRepository, ClassTimeTableMapper mapper, SectionsService sectionsService, SubjectsService subjectsService, TeachersService teachersService) {
        this.classTimetableRepository = classTimetableRepository;
        this.mapper = mapper;
        this.sectionsService = sectionsService;
        this.subjectsService = subjectsService;
        this.teachersService = teachersService;
    }

    @Transactional(readOnly = true)
    @Override
    public ClassTimetable getClassTimeTableEntity(UUID id) {

        log.debug("Fetching timetable entity with id={}", id);

        return classTimetableRepository.findById(id).orElseThrow(() -> {
            log.warn("Timetable not found with id={}", id);
            return new ResourceNotFoundException("Timetable not found with id: " + id);
        });
    }


    @Override
    @Transactional(readOnly = true)
    public ClassTimeTableResponseDto findById(UUID id) {
        ClassTimetable timetable = getClassTimeTableEntity(id);
        return mapper.toResponse(timetable);
    }

    @Override
    @Transactional
    public ClassTimeTableResponseDto create(ClassTimeTableRequestDto dto) {

        log.info("Creating timetable entry for sectionId={}, day={}, periodNumber={}", dto.getSectionId(), dto.getDay(), dto.getPeriodNumber());

        Sections section = sectionsService.getSectionEntity(dto.getSectionId());

        Subjects subject = subjectsService.getSubjectEntity(dto.getSubjectId());

        Teachers teacher = teachersService.getActiveTeacherEntity(dto.getTeacherId());


        if (classTimetableRepository.existsBySectionAndDayAndPeriodNumber(section, dto.getDay(), dto.getPeriodNumber())) {

            log.warn("Timetable already exists for sectionId={}, day={}, periodNumber={}", dto.getSectionId(), dto.getDay(), dto.getPeriodNumber());

            throw new ResourceAlreadyExistsException("A timetable entry already exists for this section, day and period.");
        }

        if (classTimetableRepository.existsByTeacherAndDayAndPeriodNumber(teacher, dto.getDay(), dto.getPeriodNumber())) {

            log.warn("Teacher timetable conflict for teacherId={}, day={}, periodNumber={}", dto.getTeacherId(), dto.getDay(), dto.getPeriodNumber());

            throw new ResourceAlreadyExistsException("This teacher is already assigned to another class for this day and period.");
        }


        ClassTimetable timetable = ClassTimetable.builder().section(section).day(dto.getDay()).periodNumber(dto.getPeriodNumber()).startTime(dto.getStartTime()).endTime(dto.getEndTime()).subject(subject).teacher(teacher).build();

        ClassTimetable saved = classTimetableRepository.save(timetable);

        log.info("Timetable created successfully with id={}", saved.getTimetableId());


        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ClassTimeTableResponseDto update(UUID id, ClassTimeTableRequestDto dto) {

        log.info("Updating timetable id={}, sectionId={}, day={}, periodNumber={}", id, dto.getSectionId(), dto.getDay(), dto.getPeriodNumber());

        ClassTimetable existingTimetable = getClassTimeTableEntity(id);

        Sections section = sectionsService.getSectionEntity(dto.getSectionId());

        Subjects subject = subjectsService.getSubjectEntity(dto.getSubjectId());

        Teachers teacher = teachersService.getActiveTeacherEntity(dto.getTeacherId());

        // Check whether another timetable occupies this section slot
        if (classTimetableRepository.existsBySectionAndDayAndPeriodNumberAndTimetableIdNot(section, dto.getDay(), dto.getPeriodNumber(), id)) {

            log.warn("Section timetable conflict while updating timetableId={}, " + "sectionId={}, day={}, periodNumber={}", id, dto.getSectionId(), dto.getDay(), dto.getPeriodNumber());

            throw new ResourceAlreadyExistsException("A timetable entry already exists for this section, day and period.");
        }

        // Check whether another timetable has the same teacher at this time
        if (classTimetableRepository.existsByTeacherAndDayAndPeriodNumberAndTimetableIdNot(teacher, dto.getDay(), dto.getPeriodNumber(), id)) {

            log.warn("Teacher timetable conflict while updating timetableId={}, " + "teacherId={}, day={}, periodNumber={}", id, dto.getTeacherId(), dto.getDay(), dto.getPeriodNumber());

            throw new ResourceAlreadyExistsException("This teacher is already assigned to another class for this day and period.");
        }

        existingTimetable.setSection(section);
        existingTimetable.setDay(dto.getDay());
        existingTimetable.setPeriodNumber(dto.getPeriodNumber());
        existingTimetable.setStartTime(dto.getStartTime());
        existingTimetable.setEndTime(dto.getEndTime());
        existingTimetable.setSubject(subject);
        existingTimetable.setTeacher(teacher);

        ClassTimetable updated = classTimetableRepository.save(existingTimetable);

        log.info("Timetable updated successfully with id={}", updated.getTimetableId());

        return mapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(UUID id) {

        ClassTimetable timetable = getClassTimeTableEntity(id);

        classTimetableRepository.delete(timetable);

        log.info("Timetable deleted successfully with id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassTimeTableResponseDto> findAll(UUID sectionId, UUID classId, UUID academicYearId) {

        log.info("Fetching class timetable. academicYearId: {}, classId: {}, sectionId: {}", academicYearId, classId, sectionId);

        // A request cannot filter by both class and section
        if (sectionId != null && classId != null) {
            log.warn("Invalid timetable filter request. Both classId and sectionId were provided. " + "classId: {}, sectionId: {}", classId, sectionId);

            throw new InvalidRequestException("Provide either classId or sectionId, not both.");
        }

        List<ClassTimetable> classTimeTables;

        if (sectionId != null) {

            log.info("Fetching timetable for sectionId: {} and academicYearId: {}", sectionId, academicYearId);

            classTimeTables = classTimetableRepository.findBySectionAndAcademicYear(sectionId, academicYearId);

        } else if (classId != null) {

            log.info("Fetching timetable for classId: {} and academicYearId: {}", classId, academicYearId);

            classTimeTables = classTimetableRepository.findByClassAndAcademicYear(classId, academicYearId);

        } else {

            log.info("Fetching all timetables for academicYearId: {}", academicYearId);

            classTimeTables = classTimetableRepository.findByAcademicYear(academicYearId);
        }

        log.info("Retrieved {} timetable entries for academicYearId: {}", classTimeTables.size(), academicYearId);

        return mapper.toResponseList(classTimeTables);
    }


    @Override
    @Transactional
    public List<ClassTimeTableResponseDto> saveBulk(List<ClassTimeTableBulkRequestDto> requests) {

        log.info("Starting bulk timetable save. entries={}", requests.size());

        Set<String> sectionSlots = new HashSet<>();
        Set<String> teacherSlots = new HashSet<>();

        List<ClassTimetable> timetables = new ArrayList<>();

        for (ClassTimeTableBulkRequestDto dto : requests) {

            log.info("Processing timetable. timetableId={}, sectionId={}, day={}, period={}", dto.getTimetableId(), dto.getSectionId(), dto.getDay(), dto.getPeriodNumber());

            /* 1. Validate duplicate SECTION slot within this request */
            String sectionSlot = dto.getSectionId() + "_" + dto.getDay() + "_" + dto.getPeriodNumber();

            if (!sectionSlots.add(sectionSlot)) {

                log.warn("Duplicate section slot found in bulk request. " + "sectionId={}, day={}, period={}", dto.getSectionId(), dto.getDay(), dto.getPeriodNumber());

                throw new ResourceAlreadyExistsException("Duplicate timetable slot found in the request for section.");
            }


            /* 2. Validate duplicate TEACHER slot within this request */
            String teacherSlot = dto.getTeacherId() + "_" + dto.getDay() + "_" + dto.getPeriodNumber();

            if (!teacherSlots.add(teacherSlot)) {

                log.warn("Duplicate teacher slot found in bulk request. " + "teacherId={}, day={}, period={}", dto.getTeacherId(), dto.getDay(), dto.getPeriodNumber());

                throw new ResourceAlreadyExistsException("Teacher is assigned to multiple classes at the same day and period.");
            }


            /* 3. Retrieve related entities*/
            Sections section = sectionsService.getSectionEntity(dto.getSectionId());

            Subjects subject = subjectsService.getSubjectEntity(dto.getSubjectId());

            Teachers teacher = teachersService.getActiveTeacherEntity(dto.getTeacherId());


            /* 4. CREATE when timetableId ==  */
            if (dto.getTimetableId() == null) {

                log.info("Creating new timetable entry. sectionId={}, day={}, period={}", dto.getSectionId(), dto.getDay(), dto.getPeriodNumber());

                // Check section conflict in database
                if (classTimetableRepository.existsBySectionAndDayAndPeriodNumber(section, dto.getDay(), dto.getPeriodNumber())) {

                    log.warn("Section timetable conflict. sectionId={}, day={}, period={}", dto.getSectionId(), dto.getDay(), dto.getPeriodNumber());

                    throw new ResourceAlreadyExistsException("A timetable entry already exists for this section, day and period.");
                }


                // Check teacher conflict in database
                if (classTimetableRepository.existsByTeacherAndDayAndPeriodNumber(teacher, dto.getDay(), dto.getPeriodNumber())) {

                    log.warn("Teacher timetable conflict. teacherId={}, day={}, period={}", dto.getTeacherId(), dto.getDay(), dto.getPeriodNumber());

                    throw new ResourceAlreadyExistsException("This teacher is already assigned to another class for this day and period.");
                }


                ClassTimetable timetable = ClassTimetable.builder().section(section).day(dto.getDay()).periodNumber(dto.getPeriodNumber()).startTime(dto.getStartTime()).endTime(dto.getEndTime()).subject(subject).teacher(teacher).build();

                timetables.add(timetable);
            }


            /* 5. UPDATE when timetableId != null */
            else {

                log.info("Updating existing timetable. timetableId={}", dto.getTimetableId());

                ClassTimetable existing = getClassTimeTableEntity(dto.getTimetableId());


                // Check section conflict excluding current timetable
                if (classTimetableRepository.existsBySectionAndDayAndPeriodNumberAndTimetableIdNot(section, dto.getDay(), dto.getPeriodNumber(), dto.getTimetableId())) {

                    log.warn("Section timetable conflict while updating. " + "timetableId={}, sectionId={}, day={}, period={}", dto.getTimetableId(), dto.getSectionId(), dto.getDay(), dto.getPeriodNumber());

                    throw new ResourceAlreadyExistsException("A timetable entry already exists for this section, day and period.");
                }


                // Check teacher conflict excluding current timetable
                if (classTimetableRepository.existsByTeacherAndDayAndPeriodNumberAndTimetableIdNot(teacher, dto.getDay(), dto.getPeriodNumber(), dto.getTimetableId())) {
                    log.warn("Teacher timetable conflict while updating. " + "timetableId={}, teacherId={}, day={}, period={}", dto.getTimetableId(), dto.getTeacherId(), dto.getDay(), dto.getPeriodNumber());
                    throw new ResourceAlreadyExistsException("This teacher is already assigned to another class for this day and period.");
                }


                existing.setSection(section);
                existing.setDay(dto.getDay());
                existing.setPeriodNumber(dto.getPeriodNumber());
                existing.setStartTime(dto.getStartTime());
                existing.setEndTime(dto.getEndTime());
                existing.setSubject(subject);
                existing.setTeacher(teacher);

                timetables.add(existing);
            }
        }


        /* 6. Save everything in one transaction */
        List<ClassTimetable> saved = classTimetableRepository.saveAll(timetables);

        log.info("Bulk timetable save completed successfully. entries={}", saved.size());

        return mapper.toResponseList(saved);
    }

}

