package com.autoecole.services;

import com.autoecole.models.Cours;
import com.autoecole.models.CoursPrive;
import com.autoecole.models.CoursPublic;
import com.autoecole.repositories.CoursRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoursService {
    private final CoursRepository coursRepository;

    public List<Cours> getUpcomingCourses() {
        return coursRepository.findByDateDebutAfter(LocalDateTime.now());
    }

    public List<Cours> getCoursesByMoniteur(Long moniteurId) {
        return coursRepository.findByMoniteurId(moniteurId);
    }

    public List<Cours> getPublicCourses() {
        return coursRepository.findAll().stream()
                .filter(cours -> cours instanceof CoursPublic)
                .toList();
    }

    public List<Cours> getPrivateCourses(Long autoEcoleId) {
        return coursRepository.findAll().stream()
                .filter(cours -> cours instanceof CoursPrive)
                .filter(cours -> ((CoursPrive) cours).getAutoEcole().getId().equals(autoEcoleId))
                .toList();
    }
} 