package com.perm.models;


import com.perm.models.base.AuditableEntity;
import com.perm.models.cours.Cours;
import com.perm.models.test.TestBlanc;
import com.perm.models.user.Admin;
import com.perm.models.user.Client;
import com.perm.models.user.Moniteur;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@Table(name = "auto_ecoles")
public class AutoEcole extends AuditableEntity {

    @Column(nullable = false)
    private String nom;

    @Column
    private String adresse;

    @Column
    private String telephone;

    @Column
    private String email;

    @Column(name = "numero_agrement")
    private String numeroAgrement;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    private Admin admin;

    @OneToMany(mappedBy = "autoEcole", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Client> clients = new ArrayList<>();

    @OneToMany(mappedBy = "autoEcole", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Moniteur> moniteurs = new ArrayList<>();

    @OneToMany(mappedBy = "autoEcole", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cours> cours = new ArrayList<>();

    @OneToMany(mappedBy = "autoEcole", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestBlanc> testsBlancs = new ArrayList<>();

    public void ajouterClient() {
        // Implémentation dans AutoEcoleService
    }

    public void ajouterMoniteur() {
        // Implémentation dans AutoEcoleService
    }

    public Map<String, Object> obtenirStatistiques() {
        // Implémentation dans StatistiqueService
        return new HashMap<>();
    }
}
