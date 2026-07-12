package com.lifetrace.backend.controller;

import com.lifetrace.backend.dto.RegisterOrganRequest;
import com.lifetrace.backend.model.Organ;
import com.lifetrace.backend.model.Recipient;
import com.lifetrace.backend.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hospital")
@RequiredArgsConstructor
@CrossOrigin
public class HospitalController {

    private final HospitalService hospitalService;

    @PreAuthorize("hasRole('HOSPITAL')")
    @PostMapping("/organs/register")
    public ResponseEntity<List<Organ>> registerOrgan(
            @RequestBody RegisterOrganRequest request
    ) {
        return ResponseEntity.ok(
                hospitalService.registerOrgan(request)
        );
    }

    @PreAuthorize("hasRole('HOSPITAL')")
    @PostMapping(value = "/profile", consumes = "multipart/form-data")
    public ResponseEntity<?> createProfile(
            @RequestParam String hospitalName,
            @RequestParam String registrationNumber,
            @RequestParam String contactNumber,
            @RequestParam String address,
            @RequestParam MultipartFile licenseFile
    ) {
        return ResponseEntity.ok(
                hospitalService.createProfile(
                        hospitalName,
                        registrationNumber,
                        contactNumber,
                        address,
                        licenseFile
                )
        );
    }

    @PreAuthorize("hasRole('HOSPITAL')")
    @PostMapping(value = "/donor/{donorId}/death-certificate", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadDeathCertificate(
            @PathVariable Long donorId,
            @RequestParam MultipartFile file
    ) {
        return ResponseEntity.ok(
                hospitalService.uploadDeathCertificate(donorId, file)
        );
    }

    // 🔥 NEW: Dashboard
    @PreAuthorize("hasRole('HOSPITAL')")
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        return ResponseEntity.ok(hospitalService.getDashboard());
    }

    // 🔥 NEW: Get all organs
    @PreAuthorize("hasRole('HOSPITAL')")
    @GetMapping("/organs")
    public ResponseEntity<List<Organ>> getMyOrgans() {
        return ResponseEntity.ok(hospitalService.getMyOrgans());
    }

    // 🔥 NEW: Get all recipients
    @PreAuthorize("hasRole('HOSPITAL')")
    @GetMapping("/recipients")
    public ResponseEntity<List<Recipient>> getMyRecipients() {
        return ResponseEntity.ok(hospitalService.getMyRecipients());
    }


    @PreAuthorize("hasRole('HOSPITAL')")
    @GetMapping("/donors/search")
    public ResponseEntity<?> searchDonor(
            @RequestParam String aadhaarNumber
    ) {
        return ResponseEntity.ok(
                hospitalService.searchDonorByAadhaar(aadhaarNumber)
        );
    }

    @PreAuthorize("hasRole('HOSPITAL')")
    @GetMapping("/profile")
    public ResponseEntity<?> getHospitalProfile() {
        return ResponseEntity.ok(
                hospitalService.getHospitalProfile()
        );
    }

}