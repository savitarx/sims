package com.invisos.sims.attendance.service;

import com.invisos.sims.attendance.model.ClassTimetable;
import com.invisos.sims.attendance.repository.ClassTimetableRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClassTimetableServiceImpl implements ClassTimetableService {

    private final ClassTimetableRepository classTimetableRepository;

    public ClassTimetableServiceImpl(ClassTimetableRepository classTimetableRepository) {
        this.classTimetableRepository = classTimetableRepository;
    }

    @Override
    public List<ClassTimetable> findAll() {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public ClassTimetable findById(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public ClassTimetable create(ClassTimetable entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public ClassTimetable update(UUID id, ClassTimetable entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void delete(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }
}
