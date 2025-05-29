package com.autoecole.controllers;

import com.autoecole.models.Cours;
import com.autoecole.models.CoursPrive;
import com.autoecole.services.CourseService;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.stripe.net.Webhook;
import com.stripe.model.Event;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/public")
    public ResponseEntity<List<Cours>> getPublicCourses() {
        List<Cours> publicCourses = courseService.getPublicCourses();
        return ResponseEntity.ok(publicCourses);
    }

    @GetMapping("/private/{autoEcoleId}")
    public ResponseEntity<List<CoursPrive>> getPrivateCoursesByAutoEcole(@PathVariable Long autoEcoleId) {
        // You will need to add security checks here to ensure the user is authorized
        List<CoursPrive> privateCourses = courseService.getPrivateCoursesByAutoEcole(autoEcoleId);
        return ResponseEntity.ok(privateCourses);
    }

    @GetMapping
    public ResponseEntity<List<Cours>> getAllCourses() {
        List<Cours> allCourses = courseService.getAllCourses();
        return ResponseEntity.ok(allCourses);
    }

    @PostMapping("/view")
    public ResponseEntity<?> recordCourseView(@RequestBody CourseViewRequest request) {
        courseService.recordCourseView(request.getClientId(), request.getCourseId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/progress/theoretical/{clientId}")
    public ResponseEntity<CourseService.TheoreticalCourseProgress> getTheoreticalCourseProgress(@PathVariable Long clientId) {
        CourseService.TheoreticalCourseProgress progress = courseService.getTheoreticalCourseProgress(clientId);
        return ResponseEntity.ok(progress);
    }

    @PostMapping("/{courseId}/create-payment-session")
    public ResponseEntity<?> createPaymentSession(@PathVariable Long courseId) {
        // Only allow payment for CoursPublic with estGratuit == false
        Cours cours = courseService.getCourseById(courseId);
        if (!(cours instanceof com.autoecole.models.CoursPublic)) {
            return ResponseEntity.badRequest().body("Payment only available for public courses.");
        }
        com.autoecole.models.CoursPublic coursPublic = (com.autoecole.models.CoursPublic) cours;
        if (Boolean.TRUE.equals(coursPublic.getEstGratuit())) {
            return ResponseEntity.badRequest().body("This course is free and does not require payment.");
        }
        try {
            Stripe.apiKey = "sk_test_51RTlUCP96G9bEJf53hJTd8YwgYzxIeL8Fw893L3s4Sv5Gpf3pOyBm03T22FnAKU3X4b2t7Qqmj7AN24BwPHdJzZZ00Xuzqtl1c";
            SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8081/payment-success?courseId=" + courseId)
                .setCancelUrl("http://localhost:8081/payment-cancel?courseId=" + courseId)
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("eur")
                                .setUnitAmount((long) (coursPublic.getPrix() * 100))
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(coursPublic.getTitre())
                                        .setDescription(coursPublic.getDescription())
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .build();
            Session session = Session.create(params);
            return ResponseEntity.ok().body(java.util.Collections.singletonMap("sessionUrl", session.getUrl()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Stripe error: " + e.getMessage());
        }
    }

    @PostMapping("/stripe/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        String endpointSecret = "sk_test_51RTlUCP96G9bEJf53hJTd8YwgYzxIeL8Fw893L3s4Sv5Gpf3pOyBm03T22FnAKU3X4b2t7Qqmj7AN24BwPHdJzZZ00Xuzqtl1c";
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            if ("checkout.session.completed".equals(event.getType())) {
                Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
                if (session != null) {
                    String successUrl = session.getSuccessUrl();
                    // Extract courseId from successUrl
                    String courseIdParam = "courseId=";
                    int idx = successUrl.indexOf(courseIdParam);
                    if (idx != -1) {
                        String idStr = successUrl.substring(idx + courseIdParam.length());
                        if (idStr.contains("&")) idStr = idStr.substring(0, idStr.indexOf("&"));
                        Long courseId = Long.parseLong(idStr);
                        // Set estGratuit to true for this course
                        com.autoecole.models.Cours cours = courseService.getCourseById(courseId);
                        if (cours instanceof com.autoecole.models.CoursPublic) {
                            com.autoecole.models.CoursPublic cp = (com.autoecole.models.CoursPublic) cours;
                            cp.setEstGratuit(true);
                            courseService.saveCourse(cp);
                        }
                    }
                }
            }
            return ResponseEntity.ok("");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Webhook error: " + e.getMessage());
        }
    }

    // Helper class for the request body of the course view endpoint
    static class CourseViewRequest {
        private Long clientId;
        private Long courseId;

        public Long getClientId() {
            return clientId;
        }

        public void setClientId(Long clientId) {
            this.clientId = clientId;
        }

        public Long getCourseId() {
            return courseId;
        }

        public void setCourseId(Long courseId) {
            this.courseId = courseId;
        }
    }
} 
