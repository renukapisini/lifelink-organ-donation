package com.lifetrace.backend.controller;

import com.lifetrace.backend.model.TransplantCase;
import com.lifetrace.backend.repository.TransplantCaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@CrossOrigin
public class PublicVerificationController {

    private final TransplantCaseRepository transplantCaseRepository;

    @GetMapping("/verify/{caseId}")
    public Map<String, Object> verify(@PathVariable Long caseId) {

        TransplantCase transplantCase = transplantCaseRepository.findById(caseId)
                .orElseThrow(() ->
                        new RuntimeException("Case not found"));

        Map<String, Object> response = new HashMap<>();

        response.put("caseId", transplantCase.getId());
        response.put("organType", transplantCase.getOrgan().getOrganType());

        response.put("organHospital",
                transplantCase.getOrganHospital().getHospitalName());

        response.put("recipientHospital",
                transplantCase.getRecipientHospital().getHospitalName());

        response.put("status", transplantCase.getStatus());
        response.put("success", transplantCase.getSuccess());

        return response;
    }
}