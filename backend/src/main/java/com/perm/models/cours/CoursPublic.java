package com.perm.models.cours;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("PUBLIC")
@Table(name = "cours_publics")
public class CoursPublic extends Cours {

    @Column(name = "nombre_vues")
    private Integer nombreVues = 0;

    public void incrementerVues() {
        this.nombreVues += 1;
    }
}
