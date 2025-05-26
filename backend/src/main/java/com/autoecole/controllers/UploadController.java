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

        try {
            String cloudinaryUrl = null;
            String fileType = null;

            // Handle file upload for images and PDFs
            if (courseUploadDto.getFile() != null && !courseUploadDto.getFile().isEmpty()) {
                logger.info("Processing file upload: {}", courseUploadDto.getFile().getOriginalFilename());
                
                // Upload file to Cloudinary
                Map uploadResult = cloudinaryService.uploadFile(courseUploadDto.getFile());
                logger.info("Cloudinary upload result: {}", uploadResult);

                cloudinaryUrl = (String) uploadResult.get("url");
                String resourceType = (String) uploadResult.get("resource_type");
                
                // Determine the correct file type
                if (courseUploadDto.getFile().getOriginalFilename() != null && 
                    courseUploadDto.getFile().getOriginalFilename().toLowerCase().endsWith(".pdf")) {
                    fileType = "raw";
                    logger.info("File identified as PDF, setting fileType to 'raw'");
                } else {
                    fileType = "image";
                    logger.info("File identified as image");
                }

                if (cloudinaryUrl == null) {
                    logger.error("Cloudinary upload failed: No URL returned.");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to get Cloudinary URL. Upload result: " + uploadResult);
                }
            } 
            // Handle video URL
            else if (courseUploadDto.getVideoUrl() != null && !courseUploadDto.getVideoUrl().isEmpty()) {
                logger.info("Processing video URL: {}", courseUploadDto.getVideoUrl());
                cloudinaryUrl = courseUploadDto.getVideoUrl();
                fileType = "video";
            }
            // No file or URL provided
            else {
                logger.warn("No file or video URL provided");
                return ResponseEntity.badRequest().body("Please provide either a file (image/PDF) or a video URL.");
            }

            logger.info("File uploaded successfully. URL: {}", cloudinaryUrl);

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

            // Set common fields from DTO and URL
            course.setTitre(courseUploadDto.getTitre());
            course.setDescription(courseUploadDto.getDescription());
            course.setCourseType(courseUploadDto.getCourseType());
            course.setCloudinaryUrl(cloudinaryUrl);
            course.setFileType(fileType);

            // Set default values
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

        } catch (IOException e) {
            logger.error("File upload error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("File upload failed: " + e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred during course upload:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + e.getMessage());
        }
    }
} 