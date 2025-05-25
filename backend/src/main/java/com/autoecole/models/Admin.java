package com.autoecole.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "admins")
@EqualsAndHashCode(callSuper = true)
public class Admin extends User {
    private String role = "ADMIN";
    private String departement;
    private String niveauAcces;
} 