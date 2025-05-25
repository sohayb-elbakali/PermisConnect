package com.autoecole.controllers;

import com.autoecole.models.Cours;
import com.autoecole.services.CoursService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cours")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
public class CoursController {
    private final CoursService coursService;

    @GetMapping("/upcoming")
    public ResponseEntity<List<Cours>> getUpcomingCourses() {
        return ResponseEntity.ok(coursService.getUpcomingCourses());
    }

    @GetMapping("/moniteur/{moniteurId}")
    public ResponseEntity<List<Cours>> getCoursesByMoniteur(@PathVariable Long moniteurId) {
        return ResponseEntity.ok(coursService.getCoursesByMoniteur(moniteurId));
    }

    @GetMapping("/public")
    public ResponseEntity<List<Cours>> getPublicCourses() {
        return ResponseEntity.ok(coursService.getPublicCourses());
    }

    @GetMapping("/private/{autoEcoleId}")
    public ResponseEntity<List<Cours>> getPrivateCourses(@PathVariable Long autoEcoleId) {
        return ResponseEntity.ok(coursService.getPrivateCourses(autoEcoleId));
    }
} 