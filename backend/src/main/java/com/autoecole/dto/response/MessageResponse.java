package com.autoecole.dto.response;

import com.autoecole.models.Message;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResponse {
    private Long id;
    private Long communicationId;
    private String contenu;
    private LocalDateTime dateEnvoi;
    private String expediteur;
    private Boolean lu;

    public static MessageResponse fromEntity(Message message) {
        MessageResponse response = new MessageResponse();
        response.setId(message.getId());
        response.setCommunicationId(message.getCommunication().getId());
        response.setContenu(message.getContenu());
        response.setDateEnvoi(message.getDateEnvoi());
        response.setExpediteur(message.getExpediteur());
        response.setLu(message.getLu());
        return response;
    }
} 