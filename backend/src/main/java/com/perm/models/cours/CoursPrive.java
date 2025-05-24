package com.perm.models.cours;


import com.perm.models.AutoEcole;
import com.perm.models.user.Client;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("PRIVE")
@Table(name = "cours_prives")
public class CoursPrive extends Cours {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auto_ecole_id", insertable = false, updatable = false)
    private AutoEcole autoEcole;

    public boolean verifierAcces(Client client) {
        // Implémentation dans CoursService
        return client.getAutoEcole() != null &&
                client.getAutoEcole().getId().equals(this.getAutoEcole().getId());
    }
}
