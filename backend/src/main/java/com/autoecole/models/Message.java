package com.autoecole.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "communication_id", nullable = false)
    private Communication communication;

    @Column(nullable = false)
    private String contenu;

    @Column(nullable = false)
    private LocalDateTime dateEnvoi;

    @Column(nullable = false)
    private String expediteur; // "CLIENT" ou "ADMIN"

    @Column(nullable = false)
    private Boolean lu;
} 