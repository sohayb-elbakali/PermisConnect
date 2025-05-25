package com.autoecole.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "communications")
public class Communication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false)
    private String sujet;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private String statut; // Ouverte, Fermée

    @OneToMany(mappedBy = "communication", cascade = CascadeType.ALL)
    private List<Message> messages;
} 