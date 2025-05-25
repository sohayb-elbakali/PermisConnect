package com.autoecole.repositories;

import com.autoecole.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByCommunicationId(Long communicationId);
    List<Message> findByCommunicationIdOrderByDateEnvoiAsc(Long communicationId);
    List<Message> findByExpediteurAndLuFalse(String expediteur);
    List<Message> findByCommunicationIdAndLuFalse(Long communicationId);
} 