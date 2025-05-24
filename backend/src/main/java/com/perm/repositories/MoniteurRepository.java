package com.perm.repositories;

import com.perm.models.user.Moniteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoniteurRepository extends JpaRepository<Moniteur, Long> {
    List<Moniteur> findByNomContainingOrPrenomContainingOrEmailContaining(String nom, String prenom, String email);
}
