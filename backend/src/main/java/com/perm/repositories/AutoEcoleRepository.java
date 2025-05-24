package com.perm.repositories;

import com.perm.models.AutoEcole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutoEcoleRepository extends JpaRepository<AutoEcole, Long> {

    Optional<AutoEcole> findByNumeroAgrement(String numeroAgrement);

    List<AutoEcole> findByNomContainingIgnoreCase(String nom);

    boolean existsByNumeroAgrement(String numeroAgrement);

    boolean existsByEmail(String email);

    @Query("SELECT ae FROM AutoEcole ae WHERE ae.adresse LIKE %:ville%")
    List<AutoEcole> findByVille(String ville);

    @Query("SELECT COUNT(c) FROM Client c WHERE c.autoEcole.id = :autoEcoleId")
    long countClientsByAutoEcoleId(Long autoEcoleId);

    @Query("SELECT COUNT(m) FROM Moniteur m WHERE m.autoEcole.id = :autoEcoleId")
    long countMoniteursByAutoEcoleId(Long autoEcoleId);
}
