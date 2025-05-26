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

    @GetMapping
    public ResponseEntity<List<Cours>> getAllCourses() {
        List<Cours> allCourses = courseService.getAllCourses();
        return ResponseEntity.ok(allCourses);
    }

    @PostMapping("/view")
    public ResponseEntity<?> recordCourseView(@RequestBody CourseViewRequest request) {
        courseService.recordCourseView(request.getClientId(), request.getCourseId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/progress/theoretical/{clientId}")
    public ResponseEntity<CourseService.TheoreticalCourseProgress> getTheoreticalCourseProgress(@PathVariable Long clientId) {
        CourseService.TheoreticalCourseProgress progress = courseService.getTheoreticalCourseProgress(clientId);
        return ResponseEntity.ok(progress);
    }

    // Helper class for the request body of the course view endpoint
    static class CourseViewRequest {
        private Long clientId;
        private Long courseId;

        public Long getClientId() {
            return clientId;
        }

        public void setClientId(Long clientId) {
            this.clientId = clientId;
        }

        public Long getCourseId() {
            return courseId;
        }

        public void setCourseId(Long courseId) {
            this.courseId = courseId;
        }
    }
} 