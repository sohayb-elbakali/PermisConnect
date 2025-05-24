package com.perm.models.test;


import com.perm.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "questions")
public class Question extends BaseEntity {

    @Column(nullable = false, columnDefinition = "TEXT")
    private String texte;

    @Column
    private String type;

    @Column(name = "reponse_correcte")
    private String reponseCorrecte;

    @Column
    private Integer points;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_blanc_id")
    private TestBlanc testBlanc;

    public boolean validerReponse(String reponse) {
        // Implémentation simple
        return reponseCorrecte != null && reponseCorrecte.equals(reponse);
    }
}
