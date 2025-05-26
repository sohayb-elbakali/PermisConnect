package com.autoecole.dto;

import com.autoecole.models.CourseType;
import org.springframework.web.multipart.MultipartFile;

public class CourseUploadDto {
    private String titre;
    private String description;
    private CourseType courseType;
    private Long autoEcoleId;
    private MultipartFile file;
    private String categorie;
    private String niveau;
    private Boolean estGratuit;

    // Getters and setters


    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

    public Long getAutoEcoleId() {
        return autoEcoleId;
    }

    public void setAutoEcoleId(Long autoEcoleId) {
        this.autoEcoleId = autoEcoleId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public Boolean getEstGratuit() {
        return estGratuit;
    }

    public void setEstGratuit(Boolean estGratuit) {
        this.estGratuit = estGratuit;
    }
} 