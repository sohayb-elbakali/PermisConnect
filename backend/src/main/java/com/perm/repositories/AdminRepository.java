package com.perm.repositories;

import com.perm.models.user.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    List<Admin> findByNomContainingOrPrenomContainingOrEmailContaining(String nom, String prenom, String email);
}
