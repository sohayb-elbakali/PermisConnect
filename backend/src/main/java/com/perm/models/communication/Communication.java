package com.perm.models.communication;


import com.perm.models.base.AuditableEntity;
import com.perm.models.user.Client;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "communications")
public class Communication extends AuditableEntity {

    @Column(nullable = false)
    private String sujet;

    @Column(nullable = false)
    private String statut; // OUVERT, EN_COURS, FERMÉ

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "communication", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    public void ajouterMessage() {
        // Implémentation dans CommunicationService
    }

    public void marquerLu() {
        // Implémentation dans CommunicationService
        for (Message message : messages) {
            message.marquerCommeLu();
        }
    }

    public void fermer() {
        this.statut = "FERMÉ";
    }
}