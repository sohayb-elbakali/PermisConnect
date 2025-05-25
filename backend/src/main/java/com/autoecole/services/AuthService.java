package com.autoecole.services;

import com.autoecole.models.Client;
import com.autoecole.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public Client login(String email, String password) {
        return clientRepository.findByUserEmail(email)
                .filter(client -> passwordEncoder.matches(password, client.getUser().getPassword()))
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }

    public boolean validateClient(Long clientId) {
        return clientRepository.existsById(clientId);
    }
} 