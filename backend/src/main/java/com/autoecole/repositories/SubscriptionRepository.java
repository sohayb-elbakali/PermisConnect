package com.autoecole.repositories;

import com.autoecole.models.AutoEcole;
import com.autoecole.models.Client;
import com.autoecole.models.Subscription;
import com.autoecole.models.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findTopByAutoEcoleAndStatusOrderByStartDateDesc(AutoEcole autoEcole, SubscriptionStatus status);

    Optional<Subscription> findTopByClientAndStatusOrderByStartDateDesc(Client client, SubscriptionStatus status);

    Optional<Subscription> findTopByAutoEcoleAndStatusOrderByEndDateDesc(AutoEcole autoEcole, SubscriptionStatus subscriptionStatus);

    Optional<Subscription> findTopByClientAndStatusOrderByEndDateDesc(Client client, SubscriptionStatus subscriptionStatus);
}