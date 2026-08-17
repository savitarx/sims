package com.invisos.sims.academic.service;

import com.invisos.sims.academic.dto.request.SectionRequestDto;
import com.invisos.sims.academic.dto.response.SectionResponseDto;
import com.invisos.sims.academic.mapper.SectionMapper;
import com.invisos.sims.academic.model.AcademicYears;
import com.invisos.sims.academic.model.Classes;
import com.invisos.sims.academic.model.Sections;
import com.invisos.sims.academic.repository.SectionsRepository;
import com.invisos.sims.common.exception.ResourceAlreadyExistsException;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import com.invisos.sims.teacher.model.Teachers;
import com.invisos.sims.teacher.service.TeachersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class SectionsServiceImpl implements SectionsService {

    private final SectionsRepository sectionsRepository;

    private final ClassesService classesService;

    private final AcademicYearsService academicYearsService;

    private final TeachersService teachersService;

    private final SectionMapper sectionMapper;

    public SectionsServiceImpl(SectionsRepository sectionsRepository, ClassesService classesService, AcademicYearsService academicYearsService1, TeachersService teachersService1, SectionMapper sectionMapper) {
        this.sectionsRepository = sectionsRepository;
        this.classesService = classesService;
        this.academicYearsService = academicYearsService1;
        this.teachersService = teachersService1;

        this.sectionMapper = sectionMapper;
    }


    @Override
    @Transactional(readOnly = true)
    public Sections getSectionEntity(UUID id) {

        log.info("Fetching section entity. sectionId={}", id);

        return sectionsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Section with given id not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public SectionResponseDto findById(UUID id) {

        log.info("Fetching section by id. sectionId={}", id);
        Sections section = getSectionEntity(id);

        return sectionMapper.toResponse(section);
    }


    @Override
    @Transactional
    public SectionResponseDto create(SectionRequestDto dto) {

        log.info("Creating section. classId={}, academicYearId={}, sectionName={}, classTeacherId={}", dto.getClassId(), dto.getAcademicYearId(), dto.getSectionName(), dto.getClassTeacherId());

        Classes schoolClass = classesService.findById(dto.getClassId());
        AcademicYears academicYear = academicYearsService.findById(dto.getAcademicYearId());
        String sectionName = dto.getSectionName().trim().toUpperCase();


        /*
         * 4. Check duplicate section
         *
         * Same class + same academic year + same section name
         * should not exist.
         */
        if (sectionsRepository.existsBySchoolClassAndAcademicYearAndSectionName(schoolClass, academicYear, sectionName)) {

            log.warn("Section already exists. classId={}, academicYearId={}, sectionName={}", dto.getClassId(), dto.getAcademicYearId(), sectionName);

            throw new ResourceAlreadyExistsException("Section " + sectionName + " already exists for this class and academic year.");
        }


        /*
         * 5. Class teacher is optional during section creation
         */
        Teachers classTeacher = null;

        if (dto.getClassTeacherId() != null) {

            log.info("Assigning class teacher while creating section. teacherId={}", dto.getClassTeacherId());
            classTeacher = teachersService.getActiveTeacherEntity(dto.getClassTeacherId());


            /*
             * 6. Check whether this teacher is already
             *    a class teacher for another section
             * one teacher can be class teacher for only one
             * section in an academic year.
             */
            if (sectionsRepository.existsByClassTeacherAndAcademicYear(classTeacher, academicYear)) {

                log.warn("Teacher is already a class teacher. teacherId={}, academicYearId={}", dto.getClassTeacherId(), dto.getAcademicYearId());

                throw new ResourceAlreadyExistsException("This teacher is already assigned as a class teacher " + "for another section in this academic year.");
            }
        }

        Sections section = Sections.builder().schoolClass(schoolClass).academicYear(academicYear).sectionName(sectionName).classTeacher(classTeacher).build();

        Sections savedSection = sectionsRepository.save(section);
        log.info("Section created successfully. sectionId={}, classId={}, academicYearId={}", savedSection.getSectionId(), dto.getClassId(), dto.getAcademicYearId());
        return sectionMapper.toResponse(savedSection);
    }

    @Override
    public SectionResponseDto assignClassTeacher(UUID sectionId, UUID teacherId) {
        Sections section = sectionsRepository.findById(sectionId).orElseThrow(() -> new ResourceNotFoundException("Section not found"));

        Teachers teacher = teachersService.getActiveTeacherEntity(teacherId);

        if (sectionsRepository.existsByClassTeacherAndAcademicYear(teacher, section.getAcademicYear())) {

            throw new ResourceAlreadyExistsException("This teacher is already assigned as a class teacher " + "for another section in this academic year.");
        }

        section.setClassTeacher(teacher);

        Sections savedSection = sectionsRepository.save(section);

        return sectionMapper.toResponse(savedSection);
    }

    @Override
    @Transactional
    public SectionResponseDto update(UUID id, SectionRequestDto dto) {

        log.info("Updating section. sectionId={}", id);

        Sections section = getSectionEntity(id);

        Classes schoolClass = classesService.findById(dto.getClassId());
        AcademicYears academicYear = academicYearsService.findById(dto.getAcademicYearId());

        String sectionName = dto.getSectionName().trim().toUpperCase();

        boolean combinationChanged = !section.getSchoolClass().getClassId().equals(dto.getClassId()) || !section.getAcademicYear().getAcademicYearId().equals(dto.getAcademicYearId()) || !section.getSectionName().equals(sectionName);

        if (combinationChanged && sectionsRepository.existsBySchoolClassAndAcademicYearAndSectionName(schoolClass, academicYear, sectionName)) {

            log.warn("Section already exists. classId={}, academicYearId={}, sectionName={}", dto.getClassId(), dto.getAcademicYearId(), sectionName);

            throw new ResourceAlreadyExistsException("Section " + sectionName + " already exists for this class and academic year.");
        }

        Teachers classTeacher = null;

        if (dto.getClassTeacherId() != null) {
            classTeacher = teachersService.getActiveTeacherEntity(
                    dto.getClassTeacherId()
            );

            boolean teacherChanged =
                    section.getClassTeacher() == null
                            || !section.getClassTeacher().getTeacherId()
                            .equals(dto.getClassTeacherId())
                            || !section.getAcademicYear()
                            .getAcademicYearId()
                            .equals(dto.getAcademicYearId());

            if (teacherChanged
                    && sectionsRepository.existsByClassTeacherAndAcademicYear(
                    classTeacher,
                    academicYear)) {

                throw new ResourceAlreadyExistsException(
                        "This teacher is already assigned as a class teacher " +
                                "for another section in this academic year."
                );
            }
        }

        section.setSchoolClass(schoolClass);
        section.setAcademicYear(academicYear);
        section.setSectionName(sectionName);
        section.setClassTeacher(classTeacher);

        Sections updatedSection = sectionsRepository.save(section);

        log.info("Section updated successfully. sectionId={}", id);

        return sectionMapper.toResponse(updatedSection);
    }

    @Override
    @Transactional
    public void delete(UUID id) {

        log.info("Deleting section. sectionId={}", id);
        Sections section = getSectionEntity(id);
        sectionsRepository.delete(section);
        log.info("Section deleted successfully. sectionId={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SectionResponseDto> findAll(UUID academicYearId, UUID classId) {

        log.info("Fetching sections. academicYearId={}, classId={}", academicYearId, classId);

        academicYearsService.findById(academicYearId);

        List<Sections> sections;

        if (classId != null) {
            classesService.findById(classId);

            sections = sectionsRepository.findAllByAcademicYear_AcademicYearIdAndSchoolClass_ClassIdOrderBySectionNameAsc(academicYearId, classId);
        } else {
            sections = sectionsRepository.findAllByAcademicYear_AcademicYearIdOrderBySchoolClass_ClassNameAscSectionNameAsc(academicYearId);
        }

        log.info("Sections fetched successfully. academicYearId={}, classId={}, count={}", academicYearId, classId, sections.size());

        return sections.stream().map(sectionMapper::toResponse).toList();
    }
}
