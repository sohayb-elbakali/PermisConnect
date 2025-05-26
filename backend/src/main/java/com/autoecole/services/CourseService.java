package com.autoecole.services;

import com.autoecole.models.*;
import com.autoecole.repositories.CoursRepository;
import com.autoecole.repositories.ClientCourseViewRepository;
import com.autoecole.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CoursRepository coursRepository;
    private final ClientCourseViewRepository clientCourseViewRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public CourseService(CoursRepository coursRepository, 
                        ClientCourseViewRepository clientCourseViewRepository,
                        ClientRepository clientRepository) {
        this.coursRepository = coursRepository;
        this.clientCourseViewRepository = clientCourseViewRepository;
        this.clientRepository = clientRepository;
    }

    public List<Cours> getPublicCourses() {
        return coursRepository.findByCourseType(CourseType.PUBLIC);
    }

    public List<CoursPrive> getPrivateCoursesByAutoEcole(Long autoEcoleId) {
        return coursRepository.findByCourseTypeAndAutoEcoleId(CourseType.PRIVATE, autoEcoleId);
    }

    public List<Cours> getAllCourses() {
        return coursRepository.findAll();
    }

    public Cours saveCourse(Cours course) {
        return coursRepository.save(course);
    }

    public void recordCourseView(Long clientId, Long courseId) {
        Optional<Client> clientOptional = clientRepository.findById(clientId);
        Optional<Cours> courseOptional = coursRepository.findById(courseId);

        if (clientOptional.isPresent() && courseOptional.isPresent()) {
            Client client = clientOptional.get();
            Cours course = courseOptional.get();

            // Check if the client has already viewed this course
            Optional<ClientCourseView> existingView = clientCourseViewRepository.findByClientIdAndCourseId(clientId, courseId);

            if (existingView.isEmpty()) {
                ClientCourseView clientCourseView = new ClientCourseView();
                clientCourseView.setClient(client);
                clientCourseView.setCourse(course);
                clientCourseView.setViewedAt(LocalDateTime.now());
                clientCourseViewRepository.save(clientCourseView);
            }
        }
    }

    public TheoreticalCourseProgress getTheoreticalCourseProgress(Long clientId) {
        // Find all theoretical courses (both public and private)
        List<Cours> allTheoreticalCourses = coursRepository.findAll().stream()
            .filter(course -> (course instanceof CoursPublic && "Théorie".equals(((CoursPublic) course).getCategorie())) ||
                             (course instanceof CoursPrive && "Théorie".equals(((CoursPrive) course).getType())))
            .collect(Collectors.toList());

        // Find courses viewed by the client
        List<ClientCourseView> clientViews = clientCourseViewRepository.findByClientId(clientId);
        Set<Long> viewedCourseIds = clientViews.stream()
            .map(view -> view.getCourse().getId())
            .collect(Collectors.toSet());

        // Count viewed theoretical courses
        long viewedTheoreticalCount = allTheoreticalCourses.stream()
            .filter(course -> viewedCourseIds.contains(course.getId()))
            .count();

        int totalTheoreticalCount = allTheoreticalCourses.size();

        return new TheoreticalCourseProgress(totalTheoreticalCount, (int) viewedTheoreticalCount);
    }

    // Helper class to return theoretical course progress
    public static class TheoreticalCourseProgress {
        private int totalTheoreticalCourses;
        private int viewedTheoreticalCourses;

        public TheoreticalCourseProgress(int totalTheoreticalCourses, int viewedTheoreticalCourses) {
            this.totalTheoreticalCourses = totalTheoreticalCourses;
            this.viewedTheoreticalCourses = viewedTheoreticalCourses;
        }

        public int getTotalTheoreticalCourses() {
            return totalTheoreticalCourses;
        }

        public int getViewedTheoreticalCourses() {
            return viewedTheoreticalCourses;
        }
    }
}
