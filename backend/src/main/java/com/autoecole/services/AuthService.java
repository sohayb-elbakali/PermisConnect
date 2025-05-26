package com.autoecole.services;

import com.autoecole.models.Client;
import com.autoecole.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final ClientRepository clientRepository;

    public Client login(String email, String password) {
        String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());
        return clientRepository.findByUserEmail(email)
                .filter(client -> encodedPassword.equals(client.getUser().getPassword()))
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }

    public boolean validateClient(Long clientId) {
        return clientRepository.existsById(clientId);
    }
} 