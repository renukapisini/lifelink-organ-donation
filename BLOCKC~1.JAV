package com.lifetrace.backend.controller;

import com.lifetrace.backend.service.BlockchainAuditService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/blockchain")
@CrossOrigin
@RequiredArgsConstructor
public class BlockchainAuditController {

    private final BlockchainAuditService auditService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_HOSPITAL')")
    @GetMapping("/verify-donor/{donorId}")
    public ResponseEntity<Map<String, Object>> verifyDonorConsent(
            @PathVariable Long donorId) {

        return ResponseEntity.ok(
                auditService.verifyDonorConsent(donorId)
        );
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_HOSPITAL')")
    @GetMapping("/verify-allocation/{organId}")
    public ResponseEntity<Map<String, Object>> verifyOrganAllocation(
            @PathVariable Long organId) {

        return ResponseEntity.ok(
                auditService.verifyOrganAllocation(organId)
        );
    }
}