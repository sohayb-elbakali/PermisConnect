package com.autoecole.controllers;

import com.autoecole.models.Cours;
import com.autoecole.models.CoursPrive;
import com.autoecole.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/public")
    public ResponseEntity<List<Cours>> getPublicCourses() {
        List<Cours> publicCourses = courseService.getPublicCourses();
        return ResponseEntity.ok(publicCourses);
    }

    @GetMapping("/private/{autoEcoleId}")
    public ResponseEntity<List<CoursPrive>> getPrivateCoursesByAutoEcole(@PathVariable Long autoEcoleId) {
        // You will need to add security checks here to ensure the user is authorized
        List<CoursPrive> privateCourses = courseService.getPrivateCoursesByAutoEcole(autoEcoleId);
        return ResponseEntity.ok(privateCourses);
    }
} 