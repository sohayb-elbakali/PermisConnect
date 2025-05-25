package com.autoecole.repositories;

import com.autoecole.models.Moniteur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MoniteurRepository extends JpaRepository<Moniteur, Long> {
    Optional<Moniteur> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Moniteur> findByAutoEcoleId(Long autoEcoleId);
    List<Moniteur> findByDisponibleTrue();
    List<Moniteur> findBySpecialite(String specialite);
} 