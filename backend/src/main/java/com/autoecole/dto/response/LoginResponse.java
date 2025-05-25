package com.autoecole.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String token;
} 