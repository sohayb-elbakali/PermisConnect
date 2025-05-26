package com.autoecole.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateReservationRequest {
    @NotNull(message = "Client ID is required")
    private Long clientId;

    @NotNull(message = "Time Slot ID is required")
    private Long timeSlotId;

    @NotNull(message = "Course ID is required")
    private Long coursId;

    private Long moniteurId;

    @NotNull(message = "Reservation date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime dateReservation;

    private String commentaire;
} 