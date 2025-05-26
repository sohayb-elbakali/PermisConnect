package com.autoecole.services;

import com.autoecole.models.Cours;
import com.autoecole.models.CourseType;
import com.autoecole.models.CoursPrive;
import com.autoecole.repositories.CoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CoursRepository coursRepository;

    @Autowired
    public CourseService(CoursRepository coursRepository) {
        this.coursRepository = coursRepository;
    }

    public List<Cours> getPublicCourses() {
        return coursRepository.findByCourseType(CourseType.PUBLIC);
    }

    public List<CoursPrive> getPrivateCoursesByAutoEcole(Long autoEcoleId) {
        return coursRepository.findByCourseTypeAndAutoEcoleId(CourseType.PRIVATE, autoEcoleId);
    }

    public Cours saveCourse(Cours course) {
        return coursRepository.save(course);
    }

    // Add methods for creating/updating courses later
} 