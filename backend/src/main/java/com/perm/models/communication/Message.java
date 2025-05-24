package com.perm.models.communication;


import com.perm.models.base.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "messages")
public class Message extends AuditableEntity {

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenu;

    @Column(name = "date_envoi")
    private LocalDateTime dateEnvoi;

    @Column
    private boolean lu = false;

    @Column(name = "expediteur_id")
    private Long expediteurId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "communication_id")
    private Communication communication;

    public void marquerCommeLu() {
        this.lu = true;
    }
}
