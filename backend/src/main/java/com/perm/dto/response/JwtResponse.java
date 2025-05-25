package com.perm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private boolean success;
    private String message;
    private String token;
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String role;
}
