package com.perm.repositories;

import com.perm.models.user.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByNomContainingOrPrenomContainingOrEmailContaining(String nom, String prenom, String email);
}