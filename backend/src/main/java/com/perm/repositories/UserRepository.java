package com.perm.repositories;

import com.perm.models.user.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Utilisateur, Long> {
    Utilisateur findByEmail(String email);
    boolean existsByEmail(String email);
}