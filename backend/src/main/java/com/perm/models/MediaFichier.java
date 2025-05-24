package com.perm.models;


import com.perm.models.base.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "media_fichiers")
public class MediaFichier extends AuditableEntity {

    @Column(nullable = false)
    private String nom;

    @Column
    private String description;

    @Column(nullable = false)
    private String type; // IMAGE, VIDEO, DOCUMENT

    @Column(nullable = false)
    private String chemin;

    @Column
    private Long taille;

    @Column(name = "type_mime")
    private String typeMime;
}
