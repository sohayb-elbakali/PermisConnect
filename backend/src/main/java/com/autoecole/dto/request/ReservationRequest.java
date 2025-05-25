package com.autoecole.dto.request;

import lombok.Data;

@Data
public class ReservationRequest {
    private Long clientId;
    private Long coursId;
    private String commentaire;
} 