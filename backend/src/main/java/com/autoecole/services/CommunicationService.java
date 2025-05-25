package com.autoecole.services;

import com.autoecole.models.Communication;
import com.autoecole.models.Message;
import com.autoecole.repositories.CommunicationRepository;
import com.autoecole.repositories.MessageRepository;
import com.autoecole.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunicationService {
    private final CommunicationRepository communicationRepository;
    private final MessageRepository messageRepository;
    private final ClientRepository clientRepository;

    @Transactional
    public Communication createCommunication(Long clientId, String sujet) {
        if (sujet == null || sujet.trim().isEmpty()) {
            throw new RuntimeException("Le sujet ne peut pas être vide");
        }

        var client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id: " + clientId));

        Communication communication = new Communication();
        communication.setClient(client);
        communication.setSujet(sujet);
        communication.setDateCreation(LocalDateTime.now());
        communication.setStatut("Ouverte");
        return communicationRepository.save(communication);
    }

    @Transactional
    public Message sendMessage(Long communicationId, String contenu, String expediteur) {
        if (contenu == null || contenu.trim().isEmpty()) {
            throw new RuntimeException("Le contenu du message ne peut pas être vide");
        }
        if (expediteur == null || expediteur.trim().isEmpty()) {
            throw new RuntimeException("L'expéditeur ne peut pas être vide");
        }

        Communication communication = communicationRepository.findById(communicationId)
                .orElseThrow(() -> new RuntimeException("Communication non trouvée avec l'id: " + communicationId));

        if ("Fermée".equals(communication.getStatut())) {
            throw new RuntimeException("Impossible d'envoyer un message à une communication fermée");
        }

        Message message = new Message();
        message.setCommunication(communication);
        message.setContenu(contenu);
        message.setDateEnvoi(LocalDateTime.now());
        message.setExpediteur(expediteur);
        message.setLu(false);

        return messageRepository.save(message);
    }

    public List<Communication> getClientCommunications(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new RuntimeException("Client non trouvé avec l'id: " + clientId);
        }
        return communicationRepository.findByClientIdOrderByDateCreationDesc(clientId);
    }

    public List<Message> getCommunicationMessages(Long communicationId) {
        if (!communicationRepository.existsById(communicationId)) {
            throw new RuntimeException("Communication non trouvée avec l'id: " + communicationId);
        }
        return messageRepository.findByCommunicationIdOrderByDateEnvoiAsc(communicationId);
    }

    @Transactional
    public void markMessageAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message non trouvé avec l'id: " + messageId));
        message.setLu(true);
        messageRepository.save(message);
    }

    @Transactional
    public void markAllMessagesAsRead(Long communicationId) {
        List<Message> messages = messageRepository.findByCommunicationIdAndLuFalse(communicationId);
        messages.forEach(message -> message.setLu(true));
        messageRepository.saveAll(messages);
    }

    @Transactional
    public void closeCommunication(Long communicationId) {
        Communication communication = communicationRepository.findById(communicationId)
                .orElseThrow(() -> new RuntimeException("Communication non trouvée avec l'id: " + communicationId));
        communication.setStatut("Fermée");
        communicationRepository.save(communication);
    }

    public List<Communication> getOpenCommunications() {
        return communicationRepository.findByStatut("Ouverte");
    }

    public List<Message> getUnreadMessages(String expediteur) {
        return messageRepository.findByExpediteurAndLuFalse(expediteur);
    }
} 