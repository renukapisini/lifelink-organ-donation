package com.lifetrace.backend.controller;

import com.lifetrace.backend.model.Recipient;
import com.lifetrace.backend.service.RecipientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hospital/recipients")
@RequiredArgsConstructor
@CrossOrigin
public class HospitalRecipientController {

    private final RecipientService recipientService;

    // ===============================
    // REGISTER RECIPIENT (HOSPITAL ONLY)
    // ===============================
    @PreAuthorize("hasRole('HOSPITAL')")
    @PostMapping("/register")
    public ResponseEntity<Recipient> registerRecipient(
            @RequestBody Recipient recipient
    ) {
        return ResponseEntity.ok(
                recipientService.registerRecipient(recipient)
        );
    }
}