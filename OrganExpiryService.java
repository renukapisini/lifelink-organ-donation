package com.lifetrace.backend.service;

import com.lifetrace.backend.model.Organ;
import com.lifetrace.backend.model.Recipient;
import com.lifetrace.backend.model.TransplantCase;
import com.lifetrace.backend.repository.OrganRepository;
import com.lifetrace.backend.repository.RecipientRepository;
import com.lifetrace.backend.repository.TransplantCaseRepository;
import com.lifetrace.backend.util.OrganStatus;
import com.lifetrace.backend.util.RecipientStatus;
import com.lifetrace.backend.util.TransplantStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganExpiryService {

    private final TransplantCaseRepository transplantCaseRepository;
    private final OrganRepository organRepository;
    private final RecipientRepository recipientRepository;

    // Run every 1 minute
    @Scheduled(fixedRate = 60000)
    public void checkExpiredOrgans() {

        List<TransplantCase> activeCases =
                transplantCaseRepository.findByStatus(TransplantStatus.IN_TRANSIT);

        for (TransplantCase transplantCase : activeCases) {

            if (transplantCase.getDispatchTime() == null) continue;

            long hoursPassed = Duration.between(
                    transplantCase.getDispatchTime(),
                    LocalDateTime.now()
            ).toHours();

            // Simple rule: expire after 4 hours
            if (hoursPassed >= 4) {

                Organ organ = transplantCase.getOrgan();
                Recipient recipient = transplantCase.getRecipient();

                organ.setStatus(OrganStatus.EXPIRED);
                recipient.setStatus(RecipientStatus.WAITING);

                transplantCase.setStatus(TransplantStatus.EXPIRED);
                transplantCase.setUpdatedAt(LocalDateTime.now());

                organRepository.save(organ);
                recipientRepository.save(recipient);
                transplantCaseRepository.save(transplantCase);

                System.out.println("Organ expired for case: " + transplantCase.getId());
            }
        }
    }
}