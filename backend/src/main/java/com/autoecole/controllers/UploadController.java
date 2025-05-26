package com.autoecole.controllers;

import com.autoecole.dto.CourseUploadDto;
import com.autoecole.models.Cours;
import com.autoecole.models.CourseType;
import com.autoecole.models.CoursPrive;
import com.autoecole.models.CoursPublic;
import com.autoecole.services.CloudinaryService;
import com.autoecole.services.CourseService;
import com.autoecole.repositories.AutoEcoleRepository;
import com.autoecole.models.AutoEcole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    private final CloudinaryService cloudinaryService;
    private final CourseService courseService;
    private final AutoEcoleRepository autoEcoleRepository;

    @Autowired
    public UploadController(CloudinaryService cloudinaryService, CourseService courseService, AutoEcoleRepository autoEcoleRepository) {
        this.cloudinaryService = cloudinaryService;
        this.courseService = courseService;
        this.autoEcoleRepository = autoEcoleRepository;
    }

    @PostMapping("/course")
    public ResponseEntity<?> uploadCourseFile(@ModelAttribute CourseUploadDto courseUploadDto) {
        logger.info("Received course upload request: {}", courseUploadDto);

        // Check if file is present
        if (courseUploadDto.getFile() == null || courseUploadDto.getFile().isEmpty()) {
            logger.warn("Upload request received with no file attached.");
            return ResponseEntity.badRequest().body("Please select a file to upload.");
        }

        try {
            // Upload file to Cloudinary
            logger.info("Attempting to upload file to Cloudinary: {}", courseUploadDto.getFile().getOriginalFilename());
            Map uploadResult = cloudinaryService.uploadFile(courseUploadDto.getFile());
            logger.info("Cloudinary upload result: {}", uploadResult);

            String cloudinaryUrl = (String) uploadResult.get("url");

            if (cloudinaryUrl == null) {
                logger.error("Cloudinary upload failed: No URL returned.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get Cloudinary URL.");
            }
            logger.info("File uploaded successfully. Cloudinary URL: {}", cloudinaryUrl);

            // Create Course entity based on type
            logger.info("Creating course entity for type: {}", courseUploadDto.getCourseType());
            Cours course;
            if (courseUploadDto.getCourseType() == CourseType.PRIVATE) {
                logger.info("Handling private course creation.");
                if (courseUploadDto.getAutoEcoleId() == null) {
                    logger.warn("AutoEcole ID is missing for private course.");
                     return ResponseEntity.badRequest().body("AutoEcole ID is required for private courses.");
                }
                logger.debug("Looking up AutoEcole with ID: {}", courseUploadDto.getAutoEcoleId());
                AutoEcole autoEcole = autoEcoleRepository.findById(courseUploadDto.getAutoEcoleId())
                        .orElseThrow(() -> {
                            logger.error("AutoEcole not found with ID: {}", courseUploadDto.getAutoEcoleId());
                            return new RuntimeException("AutoEcole not found with ID: " + courseUploadDto.getAutoEcoleId());
                        });
                logger.debug("Found AutoEcole: {}", autoEcole.getNom());

                CoursPrive privateCourse = new CoursPrive();
                privateCourse.setAutoEcole(autoEcole);
                course = privateCourse;
                logger.debug("Created CoursPrive entity.");

            } else { // PUBLIC
                logger.info("Handling public course creation.");
                 course = new CoursPublic();
                 ((CoursPublic) course).setCategorie(courseUploadDto.getCategorie());
                 ((CoursPublic) course).setNiveau(courseUploadDto.getNiveau());
                 ((CoursPublic) course).setEstGratuit(courseUploadDto.getEstGratuit());
                 logger.debug("Created CoursPublic entity.");
            }

            // Set common fields from DTO and Cloudinary URL
            course.setTitre(courseUploadDto.getTitre());
            course.setDescription(courseUploadDto.getDescription());
            course.setCourseType(courseUploadDto.getCourseType());
            course.setCloudinaryUrl(cloudinaryUrl);

            // Set default or placeholder values for mandatory fields in Cours if not in DTO
            logger.debug("Setting default/placeholder fields for course.");
            course.setDateDebut(LocalDateTime.now());
            course.setDateFin(LocalDateTime.now().plusDays(30));
            course.setCapaciteMax(100);
            course.setPrix(0.0);
            course.setMoniteur(null);


            // Save course to database
            logger.info("Saving course entity to database.");
            Cours savedCourse = courseService.saveCourse(course);
            logger.info("Course saved successfully with ID: {}", savedCourse.getId());

            return ResponseEntity.ok(savedCourse);

        } catch (Exception e) { // Catch generic Exception to get any error
            logger.error("An error occurred during course upload:", e);
            // Return a more detailed error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }
} 