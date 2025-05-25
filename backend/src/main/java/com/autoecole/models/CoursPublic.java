package com.autoecole.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "cours_publics")
public class CoursPublic extends Cours {
    
    @Column(nullable = false)
    private String categorie; // Code, Conduite, Théorie
    
    @Column(nullable = false)
    private String niveau; // Débutant, Intermédiaire, Avancé
    
    @Column(nullable = false)
    private Boolean estGratuit;
} 