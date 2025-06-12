package com.autoecole.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autoecole.models.AutoEcole;
import com.autoecole.models.Client;
import com.autoecole.models.Subscription;
import com.autoecole.models.SubscriptionStatus;
import com.autoecole.repositories.AutoEcoleRepository;
import com.autoecole.repositories.ClientRepository;
import com.autoecole.repositories.SubscriptionRepository;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final AutoEcoleRepository autoEcoleRepository;
    private final ClientRepository clientRepository;
    
    @Value("${stripe.webhook.endpoint-secret}")
    private String endpointSecret;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                              AutoEcoleRepository autoEcoleRepository,
                              ClientRepository clientRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.autoEcoleRepository = autoEcoleRepository;
        this.clientRepository = clientRepository;
    }
    
    public String getEndpointSecret() {
        return endpointSecret;
    }

    @Transactional
    public Subscription createAutoEcoleSubscription(Long autoEcoleId, String stripeSessionId) {
        AutoEcole autoEcole = autoEcoleRepository.findById(autoEcoleId)
            .orElseThrow(() -> new RuntimeException("Auto-école not found with ID: " + autoEcoleId));
            
        Subscription subscription = new Subscription(autoEcole);
        subscription.setStripeSessionId(stripeSessionId);
        
        return subscriptionRepository.save(subscription);
    }
    
    @Transactional
    public Subscription createClientSubscription(Long clientId, String stripeSessionId) {
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new RuntimeException("Client not found with ID: " + clientId));
            
        Subscription subscription = new Subscription(client);
        subscription.setStripeSessionId(stripeSessionId);
        
        return subscriptionRepository.save(subscription);
    }
    
    @Transactional
    public void activateAutoEcoleSubscription(Long autoEcoleId) {
        AutoEcole autoEcole = autoEcoleRepository.findById(autoEcoleId)
            .orElseThrow(() -> new RuntimeException("Auto-école not found with ID: " + autoEcoleId));
            
        // Find the most recent pending subscription
        Optional<Subscription> pendingSubscription = subscriptionRepository.findTopByAutoEcoleAndStatusOrderByStartDateDesc(
            autoEcole, SubscriptionStatus.PENDING);
            
        if (pendingSubscription.isPresent()) {
            Subscription subscription = pendingSubscription.get();
            subscription.activate();
            subscriptionRepository.save(subscription);
            
            // Update the auto-école to mark it as premium
            autoEcole.setPremium(true);
            autoEcoleRepository.save(autoEcole);
        }
    }
    
    @Transactional
    public void activateClientSubscription(Long clientId) {
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new RuntimeException("Client not found with ID: " + clientId));
            
        // Find the most recent pending subscription
        Optional<Subscription> pendingSubscription = subscriptionRepository.findTopByClientAndStatusOrderByStartDateDesc(
            client, SubscriptionStatus.PENDING);
            
        if (pendingSubscription.isPresent()) {
            Subscription subscription = pendingSubscription.get();
            subscription.activate();
            subscriptionRepository.save(subscription);
            
            // Update the client to mark it as premium
            client.setPremium(true);
            clientRepository.save(client);
        }
    }
    
    public SubscriptionStatus getAutoEcoleSubscriptionStatus(Long autoEcoleId) {
        AutoEcole autoEcole = autoEcoleRepository.findById(autoEcoleId)
            .orElseThrow(() -> new RuntimeException("Auto-école not found with ID: " + autoEcoleId));
        
        // Find the most recent subscription (FREE or ACTIVE)
        Optional<Subscription> latestSubscription = subscriptionRepository.findTopByAutoEcoleAndStatusOrderByEndDateDesc(
            autoEcole, SubscriptionStatus.ACTIVE);
        Optional<Subscription> latestFree = subscriptionRepository.findTopByAutoEcoleAndStatusOrderByEndDateDesc(
            autoEcole, SubscriptionStatus.FREE);
        
        if (latestSubscription.isPresent() && latestSubscription.get().isActive()) {
            return SubscriptionStatus.ACTIVE;
        } else if (latestFree.isPresent()) {
            return SubscriptionStatus.FREE;
        }
        return SubscriptionStatus.PENDING;
    }
    
    public SubscriptionStatus getClientSubscriptionStatus(Long clientId) {
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new RuntimeException("Client not found with ID: " + clientId));
        
        // Find the most recent subscription (FREE or ACTIVE)
        Optional<Subscription> latestSubscription = subscriptionRepository.findTopByClientAndStatusOrderByEndDateDesc(
            client, SubscriptionStatus.ACTIVE);
        Optional<Subscription> latestFree = subscriptionRepository.findTopByClientAndStatusOrderByEndDateDesc(
            client, SubscriptionStatus.FREE);
        
        if (latestSubscription.isPresent() && latestSubscription.get().isActive()) {
            return SubscriptionStatus.ACTIVE;
        } else if (latestFree.isPresent()) {
            return SubscriptionStatus.FREE;
        }
        return SubscriptionStatus.PENDING;
    }
    
    @Transactional
    public Subscription saveSubscription(Subscription subscription) {
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        
        // If this is an active subscription, update the entity's premium status
        if (subscription.isActive()) {
            if (subscription.getAutoEcole() != null) {
                AutoEcole autoEcole = subscription.getAutoEcole();
                autoEcole.setPremium(true);
                autoEcoleRepository.save(autoEcole);
            } else if (subscription.getClient() != null) {
                Client client = subscription.getClient();
                client.setPremium(true);
                clientRepository.save(client);
            }
        }
        
        return savedSubscription;
    }
    
    @Transactional
    public void deleteAllSubscriptions() {
        // Get all subscriptions
        List<Subscription> allSubscriptions = (List<Subscription>) subscriptionRepository.findAll();
        
        // Reset premium status for all auto-écoles and clients
        for (Subscription subscription : allSubscriptions) {
            if (subscription.getAutoEcole() != null) {
                AutoEcole autoEcole = subscription.getAutoEcole();
                autoEcole.setPremium(false);
                autoEcoleRepository.save(autoEcole);
            } else if (subscription.getClient() != null) {
                Client client = subscription.getClient();
                client.setPremium(false);
                clientRepository.save(client);
            }
        }
        
        // Delete all subscriptions
        subscriptionRepository.deleteAll();
    }
}
