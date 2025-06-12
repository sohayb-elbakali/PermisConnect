package com.autoecole.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.autoecole.models.AutoEcole;
import com.autoecole.models.Client;
import com.autoecole.models.Subscription;
import com.autoecole.models.SubscriptionStatus;
import com.autoecole.repositories.AutoEcoleRepository;
import com.autoecole.repositories.ClientRepository;
import com.autoecole.services.SubscriptionService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@RestController
@RequestMapping("/api/subscriptions")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200", "http://localhost:8081"})
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final AutoEcoleRepository autoEcoleRepository;
    private final ClientRepository clientRepository;
    
    @Value("${stripe.api.key}")
    private String stripeApiKey;
    
    @Value("${app.frontend.url}")
    private String frontendUrl;

    public SubscriptionController(SubscriptionService subscriptionService, 
                                 AutoEcoleRepository autoEcoleRepository,
                                 ClientRepository clientRepository) {
        this.subscriptionService = subscriptionService;
        this.autoEcoleRepository = autoEcoleRepository;
        this.clientRepository = clientRepository;
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(@RequestBody SubscriptionRequest request) {
        try {
            Stripe.apiKey = stripeApiKey;
            
            // Validate the request
            if (request.getAutoEcoleId() == null && request.getClientId() == null) {
                return ResponseEntity.badRequest().body("Either autoEcoleId or clientId must be provided");
            }
            
            // Determine if this is for an auto-école or a client
            String customerType;
            String customerId;
            String customerName;
            
            if (request.getAutoEcoleId() != null) {
                AutoEcole autoEcole = autoEcoleRepository.findById(request.getAutoEcoleId())
                    .orElseThrow(() -> new RuntimeException("Auto-école not found"));
                customerType = "auto_ecole";
                customerId = autoEcole.getId().toString();
                customerName = autoEcole.getNom();
            } else {
                Client client = clientRepository.findById(request.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client not found"));
                customerType = "client";
                customerId = client.getId().toString();
                customerName = client.getUser().getNom() + " " + client.getUser().getPrenom();
            }
            
            // Create Stripe checkout session
            SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(frontendUrl + "/subscription-success?type=" + customerType + "&id=" + customerId)
                .setCancelUrl(frontendUrl + "/subscription-cancel")
                .setClientReferenceId(customerType + "_" + customerId)
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("eur")
                                .setUnitAmount(1999L) // 19.99 EUR
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Premium Subscription")
                                        .setDescription("Accès à tous les cours premium pour " + customerName)
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .build();

            Session session = Session.create(params);
            
            // Return the session URL
            Map<String, String> responseData = new HashMap<>();
            responseData.put("sessionUrl", session.getUrl());
            responseData.put("sessionId", session.getId());
            
            return ResponseEntity.ok(responseData);
            
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating Stripe session: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unexpected error: " + e.getMessage());
        }
    }
    
    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, 
                                                     @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            // Process the webhook event
            com.stripe.model.Event event = com.stripe.net.Webhook.constructEvent(
                payload, sigHeader, subscriptionService.getEndpointSecret());
                
            if ("checkout.session.completed".equals(event.getType())) {
                Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
                if (session != null) {
                    String clientReferenceId = session.getClientReferenceId();
                    if (clientReferenceId != null) {
                        String[] parts = clientReferenceId.split("_");
                        if (parts.length == 2) {
                            String type = parts[0];
                            Long id = Long.parseLong(parts[1]);
                            
                            // Activate the subscription
                            if ("auto_ecole".equals(type)) {
                                subscriptionService.activateAutoEcoleSubscription(id);
                            } else if ("client".equals(type)) {
                                subscriptionService.activateClientSubscription(id);
                            }
                        }
                    }
                }
            }
            
            return ResponseEntity.ok("");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Webhook error: " + e.getMessage());
        }
    }
    
    @GetMapping("/status")
    public ResponseEntity<?> getSubscriptionStatus(@RequestParam(required = false) Long autoEcoleId,
                                                  @RequestParam(required = false) Long clientId) {
        try {
            if (autoEcoleId != null) {
                SubscriptionStatus status = subscriptionService.getAutoEcoleSubscriptionStatus(autoEcoleId);
                return ResponseEntity.ok(new SubscriptionStatusResponse(status));
            } else if (clientId != null) {
                SubscriptionStatus status = subscriptionService.getClientSubscriptionStatus(clientId);
                return ResponseEntity.ok(new SubscriptionStatusResponse(status));
            } else {
                return ResponseEntity.badRequest().body("Either autoEcoleId or clientId must be provided");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error retrieving subscription status: " + e.getMessage());
        }
    }
    
    @PostMapping("/register-free-plan")
    public ResponseEntity<?> registerFreePlan(@RequestBody SubscriptionRequest request) {
        try {
            // Validate the request
            if (request.getAutoEcoleId() == null && request.getClientId() == null) {
                return ResponseEntity.badRequest().body("Either autoEcoleId or clientId must be provided");
            }
            
            if (request.getPlanId() == null || !"free".equals(request.getPlanId())) {
                return ResponseEntity.badRequest().body("Plan ID must be 'free'");
            }
            
            Subscription subscription;
            
            if (request.getAutoEcoleId() != null) {
                AutoEcole autoEcole = autoEcoleRepository.findById(request.getAutoEcoleId())
                    .orElseThrow(() -> new RuntimeException("Auto-école not found"));
                
                // Create a free subscription for the auto-école
                subscription = new Subscription(autoEcole);
                subscription.setAmount(0.0);
                subscription.setStatus(SubscriptionStatus.FREE);
                subscription.setEndDate(null);
                subscription = subscriptionService.saveSubscription(subscription);
            } else {
                Client client = clientRepository.findById(request.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client not found"));
                
                // Create a free subscription for the client
                subscription = new Subscription(client);
                subscription.setAmount(0.0);
                subscription.setStatus(SubscriptionStatus.FREE);
                subscription.setEndDate(null);
                subscription = subscriptionService.saveSubscription(subscription);
            }
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("subscription", subscription);
            responseData.put("message", "Free plan activated successfully");
            
            return ResponseEntity.ok(responseData);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error registering free plan: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/reset-all")
    @CrossOrigin(origins = "http://localhost:8081")
    public ResponseEntity<Map<String, Object>> resetAllSubscriptions() {
        try {
            subscriptionService.deleteAllSubscriptions();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "All subscriptions have been reset successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Failed to reset subscriptions: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Request and response classes
    
    public static class SubscriptionRequest {
        private Long autoEcoleId;
        private Long clientId;
        private String planId;
        
        public Long getAutoEcoleId() {
            return autoEcoleId;
        }
        
        public void setAutoEcoleId(Long autoEcoleId) {
            this.autoEcoleId = autoEcoleId;
        }
        
        public Long getClientId() {
            return clientId;
        }
        
        public void setClientId(Long clientId) {
            this.clientId = clientId;
        }

        public String getPlanId() {
            return planId;
        }

        public void setPlanId(String planId) {
            this.planId = planId;
        }
    }
    
    public static class SubscriptionStatusResponse {
        private SubscriptionStatus status;
        
        public SubscriptionStatusResponse(SubscriptionStatus status) {
            this.status = status;
        }
        
        public SubscriptionStatus getStatus() {
            return status;
        }
        
        public void setStatus(SubscriptionStatus status) {
            this.status = status;
        }
    }
}
