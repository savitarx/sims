package com.invisos.sims.academic.service.impl;

import com.invisos.sims.academic.dto.request.SubjectRequestDto;
import com.invisos.sims.academic.mapper.SubjectMapper;
import com.invisos.sims.academic.model.Subjects;
import com.invisos.sims.academic.repository.SubjectsRepository;
import com.invisos.sims.academic.service.SubjectsService;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SubjectsServiceImpl implements SubjectsService {

    private final SubjectsRepository subjectsRepository;
    private final SubjectMapper subjectMapper;

    public SubjectsServiceImpl(SubjectsRepository subjectsRepository,
                               SubjectMapper subjectMapper) {
        this.subjectsRepository = subjectsRepository;
        this.subjectMapper = subjectMapper;
    }

    @Override
    public List<Subjects> findAll() {
        return subjectsRepository.findAll();
    }

    @Override
    public Subjects findById(UUID id) {
        return subjectsRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Subject not found with id: " + id));
    }

    @Override
    public Subjects create(SubjectRequestDto dto) {

        if (subjectsRepository.existsBySubjectCode(dto.getSubjectCode())) {
            throw new IllegalArgumentException("Subject already exists with code: " + dto.getSubjectCode());
        }

        Subjects subject = subjectMapper.toEntity(dto);
        return subjectsRepository.save(subject);
    }

    @Override
    public Subjects update(UUID id, SubjectRequestDto dto) {

        Subjects existingSubject = findById(id);

        boolean codeChanged = !dto.getSubjectCode().equals(existingSubject.getSubjectCode());
        if (codeChanged && subjectsRepository.existsBySubjectCode(dto.getSubjectCode())) {
            throw new IllegalArgumentException("Subject already exists with code: " + dto.getSubjectCode());
        }

        subjectMapper.updateEntity(dto, existingSubject);
        return subjectsRepository.save(existingSubject);
    }

    @Override
    public void delete(UUID id) {
        Subjects subject = findById(id);
        subjectsRepository.delete(subject);
    }
}
