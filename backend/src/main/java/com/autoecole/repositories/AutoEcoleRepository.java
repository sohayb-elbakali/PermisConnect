package com.autoecole.repositories;

import com.autoecole.models.AutoEcole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutoEcoleRepository extends JpaRepository<AutoEcole, Long> {
    List<AutoEcole> findByNomContainingIgnoreCase(String nom);
    List<AutoEcole> findByEmailContainingIgnoreCase(String email);
    List<AutoEcole> findByVilleContainingIgnoreCase(String ville);
    List<AutoEcole> findByCodePostal(String codePostal);
    boolean existsByEmail(String email);
    boolean existsByTelephone(String telephone);
    boolean existsBySiret(String siret);
    List<AutoEcole> findByVilleAndCodePostal(String ville, String codePostal);
} 