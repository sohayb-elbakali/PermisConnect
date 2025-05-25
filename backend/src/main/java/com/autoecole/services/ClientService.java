package com.autoecole.services;

import com.autoecole.dto.request.ClientRequest;
import com.autoecole.models.Client;
import com.autoecole.models.User;
import com.autoecole.repositories.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Client createClient(ClientRequest request) {
        if (clientRepository.existsByUserEmail(request.getEmail())) {
            throw new IllegalArgumentException("Un client avec cet email existe déjà");
        }
        if (clientRepository.existsByUserTelephone(request.getTelephone())) {
            throw new IllegalArgumentException("Un client avec ce numéro de téléphone existe déjà");
        }

        User user = new User(
            request.getNom(),
            request.getPrenom(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            request.getTelephone(),
            request.getAdresse()
        );

        Client client = new Client(
            user,
            request.getDateNaissance(),
            request.getNumeroPermis(),
            request.getTypePermis(),
            null // autoEcole will be set later if needed
        );

        return clientRepository.save(client);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client non trouvé avec l'ID: " + id));
    }

    @Transactional
    public Client updateClient(Long id, ClientRequest request) {
        Client client = getClientById(id);
        
        // Vérifier si l'email est déjà utilisé par un autre client
        if (!client.getUser().getEmail().equals(request.getEmail()) && 
            clientRepository.existsByUserEmail(request.getEmail())) {
            throw new IllegalArgumentException("Un client avec cet email existe déjà");
        }
        
        // Vérifier si le téléphone est déjà utilisé par un autre client
        if (!client.getUser().getTelephone().equals(request.getTelephone()) && 
            clientRepository.existsByUserTelephone(request.getTelephone())) {
            throw new IllegalArgumentException("Un client avec ce numéro de téléphone existe déjà");
        }

        updateClientFromRequest(client, request);
        return clientRepository.save(client);
    }

    @Transactional
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new EntityNotFoundException("Client non trouvé avec l'ID: " + id);
        }
        clientRepository.deleteById(id);
    }

    public List<Client> searchClients(String query) {
        if (query == null || query.trim().isEmpty()) {
            return clientRepository.findAll();
        }
        
        String searchTerm = query.trim();
        List<Client> results = clientRepository.findByUserNomContainingIgnoreCase(searchTerm);
        results.addAll(clientRepository.findByUserEmailContainingIgnoreCase(searchTerm));
        results.addAll(clientRepository.findByUserTelephoneContaining(searchTerm));
        
        return results.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private void updateClientFromRequest(Client client, ClientRequest request) {
        User user = client.getUser();
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setEmail(request.getEmail());
        user.setTelephone(request.getTelephone());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setAdresse(request.getAdresse());
        
        client.setDateNaissance(request.getDateNaissance());
        client.setNumeroPermis(request.getNumeroPermis());
        client.setTypePermis(request.getTypePermis());
    }
} 