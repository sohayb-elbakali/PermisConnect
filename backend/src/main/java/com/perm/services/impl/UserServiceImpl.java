package com.perm.services.impl;

import com.perm.dto.mapper.UserMapper;
import com.perm.dto.request.UserRequest;
import com.perm.dto.response.UserResponse;
import com.perm.exceptions.ResourceNotFoundException;
import com.perm.models.user.Admin;
import com.perm.models.user.Client;
import com.perm.models.user.Moniteur;
import com.perm.models.user.Utilisateur;
import com.perm.repositories.AdminRepository;
import com.perm.repositories.ClientRepository;
import com.perm.repositories.MoniteurRepository;
import com.perm.repositories.UserRepository;
import com.perm.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final ClientRepository clientRepository;
    private final MoniteurRepository moniteurRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            AdminRepository adminRepository,
            ClientRepository clientRepository,
            MoniteurRepository moniteurRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.clientRepository = clientRepository;
        this.moniteurRepository = moniteurRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse getUserById(Long id) {
        Utilisateur utilisateur = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'id : " + id));
        return userMapper.toUserResponse(utilisateur);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        Utilisateur utilisateur = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'id : " + id));

        // Mise à jour des champs communs
        utilisateur.setNom(userRequest.getNom());
        utilisateur.setPrenom(userRequest.getPrenom());
        utilisateur.setEmail(userRequest.getEmail());
        utilisateur.setTelephone(userRequest.getTelephone());

        // Si le mot de passe est fourni et différent de l'existant, on l'encode
        if (userRequest.getMotDePasse() != null && !userRequest.getMotDePasse().isEmpty()) {
            utilisateur.setMotDePasse(passwordEncoder.encode(userRequest.getMotDePasse()));
        }

        // Gestion des champs spécifiques selon le type d'utilisateur
        if (utilisateur instanceof Admin && userRequest.getAutoEcoleId() != null) {
            // Assuming Admin has a property autoEcole or similar that needs to be set
            // ((Admin) utilisateur).setAutoEcoleId(userRequest.getAutoEcoleId());
            // If this method doesn't exist, we need to adjust according to the actual Admin class structure
            Admin admin = (Admin) utilisateur;
            // Use the correct setter method from the Admin class
            // For example:
            // admin.setAutoEcole(autoEcoleService.findById(userRequest.getAutoEcoleId()));
        } else if (utilisateur instanceof Moniteur) {
            Moniteur moniteur = (Moniteur) utilisateur;
            if (userRequest.getSpecialite() != null) {
                moniteur.setSpecialite(userRequest.getSpecialite());
            }
            if (userRequest.getExperience() != null) {
                moniteur.setExperience(userRequest.getExperience());
            }
            if (userRequest.getDisponibilite() != null) {
                moniteur.setDisponibilite(userRequest.getDisponibilite());
            }
        } else if (utilisateur instanceof Client && userRequest.getStatut() != null) {
            ((Client) utilisateur).setStatut(userRequest.getStatut());
        }

        Utilisateur savedUtilisateur = userRepository.save(utilisateur);
        return userMapper.toUserResponse(savedUtilisateur);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Utilisateur non trouvé avec l'id : " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    @Transactional
    public Admin createAdmin(UserRequest adminRequest) {
        Admin admin = new Admin();
        admin.setNom(adminRequest.getNom());
        admin.setPrenom(adminRequest.getPrenom());
        admin.setEmail(adminRequest.getEmail());
        admin.setMotDePasse(passwordEncoder.encode(adminRequest.getMotDePasse()));
        admin.setTelephone(adminRequest.getTelephone());
        admin.setDateCreation(LocalDateTime.now());
        // Use the correct setter method from the Admin class
        // admin.setAutoEcoleId(adminRequest.getAutoEcoleId());
        // For example:
        // admin.setAutoEcole(autoEcoleService.findById(adminRequest.getAutoEcoleId()));

        return adminRepository.save(admin);
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    @Transactional
    public Client createClient(UserRequest clientRequest) {
        try {
            // Log all fields for debugging
            System.out.println("Creating client with data:");
            System.out.println("Nom: " + clientRequest.getNom());
            System.out.println("Prenom: " + clientRequest.getPrenom());
            System.out.println("Email: " + clientRequest.getEmail());
            System.out.println("Telephone: " + clientRequest.getTelephone());
            System.out.println("Statut: " + clientRequest.getStatut());

            // Check if email already exists using existsByEmail method
            if (userRepository.existsByEmail(clientRequest.getEmail())) {
                throw new RuntimeException("Email already in use: " + clientRequest.getEmail());
            }

            Client client = new Client();
            client.setNom(clientRequest.getNom());
            client.setPrenom(clientRequest.getPrenom());
            client.setEmail(clientRequest.getEmail());
            client.setMotDePasse(passwordEncoder.encode(clientRequest.getMotDePasse()));
            client.setTelephone(clientRequest.getTelephone());
            client.setDateCreation(LocalDateTime.now());
            client.setDateInscription(LocalDateTime.now());
            client.setStatut(clientRequest.getStatut() != null ? clientRequest.getStatut() : "ACTIF");
            client.setProgression(0.0); // Valeur initiale de la progression
            client.setAutoEcole(null); // Explicitly set to null until the client selects an auto-école

            System.out.println("Saving client...");
            return clientRepository.save(client);
        } catch (Exception e) {
            System.out.println("Error creating client: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to be handled by controller
        }
    }

    @Override
    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'id : " + id));
    }

    @Override
    public Double getClientProgression(Long id) {
        Client client = getClientById(id);
        return client.getProgression();
    }

    @Override
    public List<Moniteur> getAllMoniteurs() {
        return moniteurRepository.findAll();
    }

    @Override
    @Transactional
    public Moniteur createMoniteur(UserRequest moniteurRequest) {
        Moniteur moniteur = new Moniteur();
        moniteur.setNom(moniteurRequest.getNom());
        moniteur.setPrenom(moniteurRequest.getPrenom());
        moniteur.setEmail(moniteurRequest.getEmail());
        moniteur.setMotDePasse(passwordEncoder.encode(moniteurRequest.getMotDePasse()));
        moniteur.setTelephone(moniteurRequest.getTelephone());
        moniteur.setDateCreation(LocalDateTime.now());
        moniteur.setSpecialite(moniteurRequest.getSpecialite());
        moniteur.setExperience(moniteurRequest.getExperience());
        moniteur.setDisponibilite(moniteurRequest.getDisponibilite());

        return moniteurRepository.save(moniteur);
    }

    @Override
    public Moniteur getMoniteurById(Long id) {
        return moniteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Moniteur non trouvé avec l'id : " + id));
    }

    @Override
    @Transactional
    public Moniteur updateMoniteurDisponibilite(Long id, String disponibilite) {
        Moniteur moniteur = getMoniteurById(id);
        moniteur.setDisponibilite(disponibilite);
        return moniteurRepository.save(moniteur);
    }

    @Override
    public List<? extends Utilisateur> searchUsers(String nom, String prenom, String email, String type) {
        // Implémentation simplifiée - dans un cas réel, vous pourriez utiliser Specification ou Query JPA
        List<? extends Utilisateur> results;

        if ("ADMIN".equalsIgnoreCase(type)) {
            results = adminRepository.findByNomContainingOrPrenomContainingOrEmailContaining(
                    nom != null ? nom : "",
                    prenom != null ? prenom : "",
                    email != null ? email : "");
        } else if ("CLIENT".equalsIgnoreCase(type)) {
            results = clientRepository.findByNomContainingOrPrenomContainingOrEmailContaining(
                    nom != null ? nom : "",
                    prenom != null ? prenom : "",
                    email != null ? email : "");
        } else if ("MONITEUR".equalsIgnoreCase(type)) {
            results = moniteurRepository.findByNomContainingOrPrenomContainingOrEmailContaining(
                    nom != null ? nom : "",
                    prenom != null ? prenom : "",
                    email != null ? email : "");
        } else {
            // Si aucun type spécifié, recherche dans tous les utilisateurs
            List<Utilisateur> allUsers = new ArrayList<>();
            allUsers.addAll(adminRepository.findByNomContainingOrPrenomContainingOrEmailContaining(
                    nom != null ? nom : "",
                    prenom != null ? prenom : "",
                    email != null ? email : ""));
            allUsers.addAll(clientRepository.findByNomContainingOrPrenomContainingOrEmailContaining(
                    nom != null ? nom : "",
                    prenom != null ? prenom : "",
                    email != null ? email : ""));
            allUsers.addAll(moniteurRepository.findByNomContainingOrPrenomContainingOrEmailContaining(
                    nom != null ? nom : "",
                    prenom != null ? prenom : "",
                    email != null ? email : ""));
            results = allUsers;
        }

        return results;
    }
}
