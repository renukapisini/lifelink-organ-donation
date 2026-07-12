package com.lifetrace.backend.service;

import com.lifetrace.backend.exception.ResourceNotFoundException;
import com.lifetrace.backend.model.Hospital;
import com.lifetrace.backend.repository.HospitalRepository;
import com.lifetrace.backend.repository.OrganRepository;
import com.lifetrace.backend.repository.DonorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final HospitalRepository hospitalRepository;
    private final DonorRepository donorRepository;
    private final OrganRepository organRepository;
    private final EmailService emailService; // 🔥 added

    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }

    public List<Hospital> getPendingHospitals() {
        return hospitalRepository.findByApprovedFalse();
    }

    public Hospital approveHospital(Long hospitalId) {

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found"));

        hospital.setApproved(true);
        hospital.setBlocked(false);

        Hospital saved = hospitalRepository.save(hospital);

        try {
            emailService.sendHospitalStatusEmail(
                    hospital.getUser().getEmail(),
                    "APPROVED"
            );
        } catch (Exception e) {}

        return saved;
    }

    public Hospital blockHospital(Long hospitalId) {

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found"));

        hospital.setBlocked(true);

        Hospital saved = hospitalRepository.save(hospital);

        try {
            emailService.sendHospitalStatusEmail(
                    hospital.getUser().getEmail(),
                    "BLOCKED"
            );
        } catch (Exception e) {}

        return saved;
    }

    public Hospital unblockHospital(Long hospitalId) {

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found"));

        hospital.setBlocked(false);

        Hospital saved = hospitalRepository.save(hospital);

        try {
            emailService.sendHospitalStatusEmail(
                    hospital.getUser().getEmail(),
                    "UNBLOCKED"
            );
        } catch (Exception e) {}

        return saved;
    }

    public Map<String, Object> getAuditData() {

        Map<String, Object> data = new HashMap<>();

        data.put("totalDonors", donorRepository.count());
        data.put("totalHospitals", hospitalRepository.count());
        data.put("totalOrgans", organRepository.count());

        return data;
    }
}