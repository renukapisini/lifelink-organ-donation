package com.lifetrace.backend.controller;

import com.lifetrace.backend.model.Hospital;
import com.lifetrace.backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin
public class AdminController {

    private final AdminService adminService;

    // ==============================================
    // 1️⃣ Get All Hospitals
    // ==============================================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/hospitals")
    public ResponseEntity<List<Hospital>> getHospitals() {
        return ResponseEntity.ok(adminService.getAllHospitals());
    }

    // ==============================================
    // 2️⃣ Get Pending Hospitals
    // ==============================================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/hospitals/pending")
    public ResponseEntity<List<Hospital>> getPendingHospitals() {
        return ResponseEntity.ok(adminService.getPendingHospitals());
    }

    // ==============================================
    // 3️⃣ Approve Hospital
    // ==============================================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/approve-hospital/{id}")
    public ResponseEntity<Hospital> approveHospital(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.approveHospital(id));
    }

    // ==============================================
    // 4️⃣ Block Hospital
    // ==============================================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/block-hospital/{id}")
    public ResponseEntity<Hospital> blockHospital(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.blockHospital(id));
    }

    // ==============================================
    // 5️⃣ Unblock Hospital
    // ==============================================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/unblock-hospital/{id}")
    public ResponseEntity<Hospital> unblockHospital(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.unblockHospital(id));
    }

    // ==============================================
    // 6️⃣ Audit Data
    // ==============================================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/audit")
    public ResponseEntity<?> audit() {
        return ResponseEntity.ok(adminService.getAuditData());
    }
}