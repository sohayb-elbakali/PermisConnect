package com.autoecole.repositories;

import com.autoecole.models.Communication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunicationRepository extends JpaRepository<Communication, Long> {
    List<Communication> findByClientId(Long clientId);
    List<Communication> findByClientIdOrderByDateCreationDesc(Long clientId);
    List<Communication> findByStatut(String statut);
} 