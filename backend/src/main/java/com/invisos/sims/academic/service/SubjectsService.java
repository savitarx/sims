package com.invisos.sims.academic.service;

import com.invisos.sims.academic.dto.request.SubjectRequestDto;
import com.invisos.sims.academic.model.Subjects;

import java.util.List;
import java.util.UUID;

public interface SubjectsService {

    List<Subjects> findAll();

    Subjects findById(UUID id);

    Subjects create(SubjectRequestDto request);

    Subjects update(UUID id, SubjectRequestDto request);

    void delete(UUID id);

}
