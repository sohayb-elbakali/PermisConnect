package com.autoecole.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.autoecole.models.AutoEcole;

@Data
@NoArgsConstructor
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "date_naissance")
    private String dateNaissance;

    @Column(name = "numero_permis")
    private String numeroPermis;

    @Column(name = "type_permis")
    private String typePermis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auto_ecole_id")
    private AutoEcole autoEcole;

    public Client(User user, String dateNaissance, String numeroPermis, String typePermis, AutoEcole autoEcole) {
        this.user = user;
        this.dateNaissance = dateNaissance;
        this.numeroPermis = numeroPermis;
        this.typePermis = typePermis;
        this.autoEcole = autoEcole;
    }
}

