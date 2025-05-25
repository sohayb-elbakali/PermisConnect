package com.autoecole.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@Entity
@Table(name = "moniteurs")
@EqualsAndHashCode(callSuper = true)
public class Moniteur extends User {
    private String role = "MONITEUR";
    private String specialite;
    private Integer experienceAnnees;
    private String numeroAgrement;
    private Boolean disponible = true;

    @ManyToOne
    @JoinColumn(name = "auto_ecole_id")
    private AutoEcole autoEcole;

    @OneToMany(mappedBy = "moniteur")
    private List<Cours> cours;
} 