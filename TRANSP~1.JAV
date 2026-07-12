package com.lifetrace.backend.controller;

import com.lifetrace.backend.model.TransplantCase;
import com.lifetrace.backend.service.TransplantCaseService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transplant")
@RequiredArgsConstructor
@CrossOrigin
public class TransplantCaseController {

    private final TransplantCaseService transplantCaseService;

    // ============================================================
    // DISPATCH ORGAN (Organ hospital only)
    // ============================================================

    @PreAuthorize("hasRole('HOSPITAL')")
    @PostMapping("/{caseId}/dispatch")
    public ResponseEntity<TransplantCase> dispatch(@PathVariable Long caseId) {
        return ResponseEntity.ok(
                transplantCaseService.dispatch(caseId)
        );
    }

    // ============================================================
    // RECEIVE ORGAN (Recipient hospital only)
    // ============================================================

    @PreAuthorize("hasRole('HOSPITAL')")
    @PostMapping("/{caseId}/receive")
    public ResponseEntity<TransplantCase> receive(@PathVariable Long caseId) {
        return ResponseEntity.ok(
                transplantCaseService.receive(caseId)
        );
    }

    // ============================================================
    // START SURGERY
    // ============================================================

    @PreAuthorize("hasRole('HOSPITAL')")
    @PostMapping("/{caseId}/start-surgery")
    public ResponseEntity<TransplantCase> startSurgery(@PathVariable Long caseId) {
        return ResponseEntity.ok(
                transplantCaseService.startSurgery(caseId)
        );
    }

    // ============================================================
    // COMPLETE SURGERY
    // ============================================================

    @PreAuthorize("hasRole('HOSPITAL')")
    @PostMapping("/{caseId}/complete-surgery")
    public ResponseEntity<TransplantCase> completeSurgery(
            @PathVariable Long caseId,
            @RequestBody SurgeryRequest request
    ) {
        return ResponseEntity.ok(
                transplantCaseService.completeSurgery(
                        caseId,
                        request.isSuccess(),
                        request.getNotes()
                )
        );
    }

    // ============================================================
    // REQUEST BODY
    // ============================================================

    @Data
    public static class SurgeryRequest {
        private boolean success;
        private String notes;
    }

    // ============================================================
    // GET TIMELINE
    // ============================================================

//    @PreAuthorize("hasAnyRole('ADMIN','HOSPITAL')")
    @GetMapping("/{caseId}/timeline")
    public ResponseEntity<?> getTimeline(@PathVariable Long caseId) {
        return ResponseEntity.ok(
                transplantCaseService.getTimeline(caseId)
        );
    }

    // ============================================================
    // GET ALL CASES
    // ============================================================

    @PreAuthorize("hasAnyRole('ADMIN','HOSPITAL')")
    @GetMapping("/cases")
    public ResponseEntity<?> getAllCases() {
        return ResponseEntity.ok(
                transplantCaseService.getAllCases()
        );
    }

}