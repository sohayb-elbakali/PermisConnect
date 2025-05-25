package com.autoecole.dto.response;

import com.autoecole.models.Communication;
import com.autoecole.dto.response.MessageResponse;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CommunicationResponse {
    private Long id;
    private Long clientId;
    private String clientNom;
    private String sujet;
    private LocalDateTime dateCreation;
    private String statut;
    private List<MessageResponse> messages;
    private int nombreMessagesNonLus;

    public static CommunicationResponse fromEntity(Communication communication) {
        CommunicationResponse response = new CommunicationResponse();
        response.setId(communication.getId());
        response.setClientId(communication.getClient().getId());
        response.setClientNom(communication.getClient().getUser().getNom() + " " + communication.getClient().getUser().getPrenom());
        response.setSujet(communication.getSujet());
        response.setDateCreation(communication.getDateCreation());
        response.setStatut(communication.getStatut());
        
        if (communication.getMessages() != null) {
            response.setMessages(communication.getMessages().stream()
                    .map(MessageResponse::fromEntity)
                    .collect(Collectors.toList()));
            
            response.setNombreMessagesNonLus((int) communication.getMessages().stream()
                    .filter(message -> !message.getLu())
                    .count());
        }
        
        return response;
    }
} 