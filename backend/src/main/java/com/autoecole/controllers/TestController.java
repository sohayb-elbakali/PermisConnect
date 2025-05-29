package com.autoecole.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/api/ping")
    public String ping() {
        return "pong";
    }
} 
