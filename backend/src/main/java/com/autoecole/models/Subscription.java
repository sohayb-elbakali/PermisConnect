package com.autoecole.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
@Data
@NoArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "auto_ecole_id")
    private AutoEcole autoEcole;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String stripeSessionId;
    private String stripeCustomerId;
    private Double amount;
    private String currency;

    public Subscription(AutoEcole autoEcole) {
        this.autoEcole = autoEcole;
        this.status = SubscriptionStatus.PENDING;
        this.startDate = LocalDateTime.now();
        this.currency = "EUR";
        this.amount = 19.99;
    }

    public Subscription(Client client) {
        this.client = client;
        this.status = SubscriptionStatus.PENDING;
        this.startDate = LocalDateTime.now();
        this.currency = "EUR";
        this.amount = 19.99;
    }

    public void activate() {
        this.status = SubscriptionStatus.ACTIVE;
        this.endDate = LocalDateTime.now().plusMonths(1);
    }

    public boolean isActive() {
        return status == SubscriptionStatus.ACTIVE && 
               endDate != null && 
               LocalDateTime.now().isBefore(endDate);
    }
}
