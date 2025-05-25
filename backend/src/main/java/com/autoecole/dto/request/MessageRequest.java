package com.autoecole.dto.request;

import lombok.Data;

@Data
public class MessageRequest {
    private Long communicationId;
    private String contenu;
    private String expediteur;
} 