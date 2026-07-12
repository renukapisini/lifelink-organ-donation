package com.lifetrace.backend.controller;

import com.lifetrace.backend.dto.DonorRequest;
import com.lifetrace.backend.service.DonorService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/donor")
@CrossOrigin
public class DonorController {

    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    // ===============================
    // Create Donor Profile
    // ===============================
    @PreAuthorize("hasRole('DONOR')")
    @PostMapping("/profile")
    public ResponseEntity<?> createDonorProfile(
            @RequestBody DonorRequest request
    ) {
        return ResponseEntity.ok(
                donorService.createDonorProfile(request)
        );
    }

    // ===============================
    // Upload Donor Consent
    // Flow handled completely by service:
    // IPFS → Blockchain → DB
    // ===============================
    @PreAuthorize("hasRole('DONOR')")
    @PostMapping(
            value = "/upload-consent/{donorId}",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<?> uploadConsent(
            @PathVariable Long donorId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            String ipfsHash = donorService.uploadConsent(donorId, file);
            return ResponseEntity.ok(
                    "Donor consent uploaded successfully\nIPFS Hash: " + ipfsHash
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PreAuthorize("hasRole('DONOR')")
    @GetMapping("/profile")
    public ResponseEntity<?> getDonorProfile() {
        return ResponseEntity.ok(
                donorService.getDonorProfile()
        );
    }

}