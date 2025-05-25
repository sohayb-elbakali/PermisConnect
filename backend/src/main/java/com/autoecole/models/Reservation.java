package com.autoecole.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "cours_id", nullable = false)
    private Cours cours;

    @Column(nullable = false)
    private LocalDateTime dateReservation;

    @Column(nullable = false)
    private String statut; // En attente, Confirmée, Annulée

    @Column
    private String commentaire;
} 