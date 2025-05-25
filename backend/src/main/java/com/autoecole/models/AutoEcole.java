package com.autoecole.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "auto_ecoles")
public class AutoEcole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String telephone;

    @Column(nullable = false)
    private String adresse;

    @Column(nullable = false, unique = true)
    private String siret;

    private String codePostal;

    private String ville;

    private String siteWeb;

    @Column(length = 1000)
    private String description;

    private String horaires;

    @OneToMany(mappedBy = "autoEcole")
    private List<Client> clients;

    @OneToMany(mappedBy = "autoEcole")
    private List<CoursPrive> coursPrives;

    @OneToMany(mappedBy = "autoEcole")
    private List<Moniteur> moniteurs;
} 