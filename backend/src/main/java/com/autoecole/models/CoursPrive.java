package com.autoecole.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "cours_prives")
public class CoursPrive extends Cours {
    
    @ManyToOne
    @JoinColumn(name = "auto_ecole_id", nullable = false)
    private AutoEcole autoEcole;
    
    @Column(nullable = false)
    private String niveau; // Débutant, Intermédiaire, Avancé
    
    @Column(nullable = false)
    private String type; // Code, Conduite, Théorie
} 