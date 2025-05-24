package com.perm.models.user;

import com.perm.models.base.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type_utilisateur")
@Table(name = "utilisateurs")
public abstract class Utilisateur extends AuditableEntity {

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @Column
    private String telephone;

    // Méthodes du diagramme de classe - implémentées dans les services
    public boolean authentifier() {
        // Implémentation à faire dans le service d'authentification
        return false;
    }

    public void modifierProfil() {
        // Implémentation à faire dans le service utilisateur
    }

    public Utilisateur obtenirInformations() {
        // Implémentation à faire dans le service utilisateur
        return this;
    }
}