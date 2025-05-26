package com.autoecole.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonBackReference(value = "client-reservations")
    @NotNull
    private Client client;

    @ManyToOne
    @JoinColumn(name = "moniteur_id")
    @JsonBackReference(value = "moniteur-reservations")
    private Moniteur moniteur;

    @OneToOne
    @JoinColumn(name = "time_slot_id")
    private TimeSlot timeSlot;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @NotNull
    private LocalDateTime dateReservation;

    @Column(length = 500)
    private String commentaire;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private ReservationStatus statut = ReservationStatus.PENDING;

    public enum ReservationStatus {
        PENDING,
        BOOKED,
        CANCELLED
    }
} 