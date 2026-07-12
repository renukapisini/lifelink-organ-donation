package com.lifetrace.backend.service;

import com.lifetrace.backend.exception.ResourceNotFoundException;
import com.lifetrace.backend.exception.UnauthorizedException;
import com.lifetrace.backend.model.Hospital;
import com.lifetrace.backend.model.Recipient;
import com.lifetrace.backend.model.User;
import com.lifetrace.backend.repository.HospitalRepository;
import com.lifetrace.backend.repository.RecipientRepository;
import com.lifetrace.backend.repository.UserRepository;
import com.lifetrace.backend.util.RecipientStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecipientService {

    private final RecipientRepository recipientRepository;
    private final HospitalRepository hospitalRepository;
    private final UserRepository userRepository;
    private final OrganService organService;

    @Transactional
    public Recipient registerRecipient(Recipient recipient) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Hospital hospital = hospitalRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not registered"));

        if (!hospital.isApproved()) {
            throw new UnauthorizedException("Hospital is not approved by admin");
        }

        recipient.setHospital(hospital);
        recipient.setLocation(hospital.getAddress());

        // ✅ ENUM FIX
        recipient.setStatus(RecipientStatus.WAITING);

        Recipient savedRecipient = recipientRepository.save(recipient);

        organService.tryMatchByRecipient(savedRecipient);

        return savedRecipient;
    }
}
