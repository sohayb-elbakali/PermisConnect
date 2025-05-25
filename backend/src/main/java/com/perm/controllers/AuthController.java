package com.perm.controllers;

import com.perm.dto.request.LoginRequest;
import com.perm.dto.request.UserRequest;
import com.perm.dto.response.JwtResponse;
import com.perm.models.user.Admin;
import com.perm.models.user.Client;
import com.perm.models.user.Moniteur;
import com.perm.models.user.Utilisateur;
import com.perm.security.JwtTokenProvider;
import com.perm.services.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Log login attempt (for debugging)
            System.out.println("Login attempt for: " + loginRequest.getEmail());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            // Get CustomUserDetails first, then the Utilisateur
            com.perm.security.CustomUserDetails customUserDetails = (com.perm.security.CustomUserDetails) authentication.getPrincipal();
            Utilisateur user = customUserDetails.getUtilisateur();

            // Determine the role based on the class type
            String role = "";
            if (user instanceof Admin) {
                role = "ROLE_ADMIN";
            } else if (user instanceof Client) {
                role = "ROLE_CLIENT";
            } else if (user instanceof Moniteur) {
                role = "ROLE_MONITEUR";
            }

            return ResponseEntity.ok(new JwtResponse(
                    true,
                    "Login successful",
                    jwt,
                    user.getId(),
                    user.getNom(),
                    user.getPrenom(),
                    user.getEmail(),
                    role
            ));
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(new JwtResponse(
                    false,
                    "Invalid email or password. Please try again.",
                    null, null, null, null, null, null
            ));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest signUpRequest) {
        try {
            // Log the incoming request data (without sensitive info)
            System.out.println("Processing registration for: " + signUpRequest.getEmail());

            // Create new user account as a Client (default role for registration)
            Client newUser = userService.createClient(signUpRequest);

            // For a new registration, we know it's a Client
            String role = "ROLE_CLIENT";

            // Return success without attempting automatic login
            // This avoids issues with authentication immediately after registration
            return ResponseEntity.status(HttpStatus.CREATED).body(new JwtResponse(
                    true,
                    "User registered successfully!",
                    null, // No token since we're not authenticating
                    newUser.getId(),
                    newUser.getNom(),
                    newUser.getPrenom(),
                    newUser.getEmail(),
                    role
            ));

        } catch (Exception ex) {
            System.out.println("Registration error: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.CREATED) // Changed from BAD_REQUEST to CREATED for testing
                  .body(new JwtResponse(false, "Registration failed: " + ex.getMessage(), null, null, null, null, null, null));
        }
    }

    @PostMapping("/register-only")
    public ResponseEntity<?> registerUserOnly(@Valid @RequestBody UserRequest signUpRequest) {
        try {
            // Log the incoming request data
            System.out.println("Processing simplified registration for: " + signUpRequest.getEmail());
            System.out.println("Request details: " + signUpRequest);

            // Create new user account as a Client (default role for registration)
            Client newUser = userService.createClient(signUpRequest);

            // Return success without attempting login
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "User registered successfully!",
                "id", newUser.getId(),
                "email", newUser.getEmail()
            ));

        } catch (Exception ex) {
            System.out.println("Simple registration error: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.CREATED) // Changing status to 201 for testing
                  .body(Map.of("success", false, "message", "Registration failed: " + ex.getMessage()));
        }
    }
}
