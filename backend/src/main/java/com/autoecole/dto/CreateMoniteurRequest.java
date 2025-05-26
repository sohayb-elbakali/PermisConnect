package com.autoecole.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateMoniteurRequest {
    @NotBlank(message = "First name is required")
    private String nom;

    @NotBlank(message = "Last name is required")
    private String prenom;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Phone number is required")
    private String telephone;

    @NotBlank(message = "Address is required")
    private String adresse;

    @NotBlank(message = "Specialty is required")
    private String specialite;

    @NotNull(message = "Years of experience is required")
    @Positive(message = "Years of experience must be positive")
    private Integer experienceAnnees;

    @NotBlank(message = "Agreement number is required")
    private String numeroAgrement;

    private Long autoEcoleId;
} 