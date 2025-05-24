package com.perm.models.cours;


import com.perm.models.AutoEcole;
import com.perm.models.base.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type_cours")
@Table(name = "cours")
public abstract class Cours extends AuditableEntity {

    @Column(nullable = false)
    private String titre;

    @Column
    private String description;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    @Column
    private Integer duree;

    @Column
    private boolean actif = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auto_ecole_id")
    private AutoEcole autoEcole;

    public String consulter() {
        // Implémentation dans CoursService
        return this.contenu;
    }

    public void modifier() {
        // Implémentation dans CoursService
    }

}
