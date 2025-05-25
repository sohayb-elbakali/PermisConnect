package com.autoecole.services;

import com.autoecole.models.Admin;
import com.autoecole.repositories.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    public Optional<Admin> getAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    @Transactional
    public Admin createAdmin(Admin admin) {
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        return adminRepository.save(admin);
    }

    @Transactional
    public Admin updateAdmin(Long id, Admin adminDetails) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        admin.setNom(adminDetails.getNom());
        admin.setPrenom(adminDetails.getPrenom());
        admin.setEmail(adminDetails.getEmail());
        admin.setDepartement(adminDetails.getDepartement());
        admin.setNiveauAcces(adminDetails.getNiveauAcces());
        
        return adminRepository.save(admin);
    }

    @Transactional
    public void deleteAdmin(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new RuntimeException("Admin not found");
        }
        adminRepository.deleteById(id);
    }
} 