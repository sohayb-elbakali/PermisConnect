package com.autoecole.repositories;

import com.autoecole.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByUserEmail(String email);
    boolean existsByUserTelephone(String telephone);
    Optional<Client> findByUserEmail(String email);
    List<Client> findByUserNomContainingIgnoreCase(String nom);
    List<Client> findByUserEmailContainingIgnoreCase(String email);
    List<Client> findByUserTelephoneContaining(String telephone);
} 