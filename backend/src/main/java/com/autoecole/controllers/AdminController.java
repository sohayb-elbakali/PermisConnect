package com.autoecole.controllers;

import com.autoecole.models.Admin;
import com.autoecole.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
public class AdminController {
    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        return adminService.getAdminById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Admin> getAdminByEmail(@PathVariable String email) {
        return adminService.getAdminByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Admin> createAdmin(@RequestBody Admin admin) {
        return ResponseEntity.ok(adminService.createAdmin(admin));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable Long id, @RequestBody Admin admin) {
        return ResponseEntity.ok(adminService.updateAdmin(id, admin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.ok().build();
    }
} 