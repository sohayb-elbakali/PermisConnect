package com.autoecole.controllers;

import com.autoecole.dto.request.LoginRequest;
import com.autoecole.models.Client;
import com.autoecole.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Client> login(@RequestBody LoginRequest request) {
        Client client = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(client);
    }

    @GetMapping("/validate/{clientId}")
    public ResponseEntity<Boolean> validateClient(@PathVariable Long clientId) {
        boolean isValid = authService.validateClient(clientId);
        return ResponseEntity.ok(isValid);
    }
} 