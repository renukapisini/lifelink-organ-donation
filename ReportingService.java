package com.lifetrace.backend.service;

import com.lifetrace.backend.repository.*;
import com.lifetrace.backend.util.OrganStatus;
import com.lifetrace.backend.util.TransplantStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportingService {

    private final OrganRepository organRepository;
    private final RecipientRepository recipientRepository;
    private final TransplantCaseRepository transplantCaseRepository;

    public Map<String, Object> getAdminStats() {

        Map<String, Object> stats = new HashMap<>();

        // Organ stats
        stats.put("totalOrgans", organRepository.count());
        stats.put("availableOrgans",
                organRepository.countByStatus(OrganStatus.AVAILABLE));
        stats.put("retrievedOrgans",
                organRepository.countByStatus(OrganStatus.RETRIEVED));

        // Recipient stats
        stats.put("totalRecipients", recipientRepository.count());

        // Case stats
        stats.put("totalCases", transplantCaseRepository.count());
        stats.put("activeCases",
                transplantCaseRepository.countByStatus(TransplantStatus.IN_TRANSIT));
        stats.put("completedCases",
                transplantCaseRepository.countByStatus(TransplantStatus.COMPLETED));
        stats.put("failedCases",
                transplantCaseRepository.countByStatus(TransplantStatus.FAILED));

        return stats;
    }
}
