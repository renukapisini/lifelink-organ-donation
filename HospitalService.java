package com.lifetrace.backend.service;

import com.lifetrace.backend.controller.HospitalDonorResponse;
import com.lifetrace.backend.dto.HospitalProfileResponse;
import com.lifetrace.backend.dto.RegisterOrganRequest;
import com.lifetrace.backend.dto.Response;
import com.lifetrace.backend.exception.BadRequestException;
import com.lifetrace.backend.exception.ResourceNotFoundException;
import com.lifetrace.backend.model.*;
import com.lifetrace.backend.repository.*;
import com.lifetrace.backend.util.OrganStatus;
import com.lifetrace.backend.util.RecipientStatus;
import com.lifetrace.backend.util.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final OrganService organService;
    private final HospitalRepository hospitalRepository;
    private final UserRepository userRepository;
    private final IpfsService ipfsService;
    private final DonorRepository donorRepository;
    private final OrganRepository organRepository;
    private final RecipientRepository recipientRepository;

    // =========================================================
    // CREATE PROFILE
    // =========================================================
    public String createProfile(
            String hospitalName,
            String registrationNumber,
            String contactNumber,
            String address,
            MultipartFile licenseFile
    ) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (hospitalRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("Hospital profile already exists");
        }

        String ipfsHash = ipfsService.uploadFile(licenseFile);

        Hospital hospital = new Hospital();
        hospital.setHospitalName(hospitalName);
        hospital.setRegistrationNumber(registrationNumber);
        hospital.setContactNumber(contactNumber);
        hospital.setAddress(address);
        hospital.setEmail(email);
        hospital.setLicenseUrl(ipfsHash);
        hospital.setApproved(false);
        hospital.setBlocked(false);
        hospital.setUser(user);

        hospitalRepository.save(hospital);

        return "Profile submitted. Waiting for admin approval.";
    }

    // =========================================================
    // UPLOAD DEATH CERTIFICATE
    // =========================================================
    public String uploadDeathCertificate(Long donorId, MultipartFile file) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Hospital hospital = hospitalRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Hospital profile not found"));

        if (!hospital.isApproved())
            throw new RuntimeException("Hospital not approved by admin");

        if (hospital.isBlocked())
            throw new RuntimeException("Hospital is blocked by admin");

        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Donor not found"));

        String ipfsHash = ipfsService.uploadFile(file);

        donor.setDeathCertificateUrl(ipfsHash);
        donor.setDeceased(true);
        donor.setDeclaredByHospitalId(hospital.getId());

        donorRepository.save(donor);

        return "Death certificate uploaded successfully.";
    }

    // =========================================================
    // REGISTER ORGAN
    // =========================================================
    public List<Organ> registerOrgan(RegisterOrganRequest request) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Hospital hospital = hospitalRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Hospital profile not found"));

        if (!hospital.isApproved())
            throw new RuntimeException("Hospital not approved by admin");

        if (hospital.isBlocked())
            throw new RuntimeException("Hospital is blocked by admin");

        Donor donor = donorRepository.findById(request.getDonorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Donor not found"));

        if (!donor.isDeceased())
            throw new RuntimeException("Donor is not marked as deceased.");

        return organService.registerOrgan(request);
    }

    // =========================================================
    // DASHBOARD
    // =========================================================
    public Map<String, Object> getDashboard() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Hospital hospital = hospitalRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital profile not found"));

        Map<String, Object> data = new HashMap<>();

        data.put("totalOrgans", organRepository.countByHospital(hospital));
        data.put("availableOrgans",
                organRepository.countByHospitalAndStatus(hospital, OrganStatus.AVAILABLE));
        data.put("allocatedOrgans",
                organRepository.countByHospitalAndStatus(hospital, OrganStatus.ALLOCATED));

        data.put("totalRecipients", recipientRepository.countByHospital(hospital));
        data.put("waitingRecipients",
                recipientRepository.countByHospitalAndStatus(hospital, RecipientStatus.WAITING));
        data.put("matchedRecipients",
                recipientRepository.countByHospitalAndStatus(hospital, RecipientStatus.MATCHED));

        return data;
    }

    // =========================================================
    // GET MY ORGANS
    // =========================================================
    public List<Organ> getMyOrgans() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Hospital hospital = hospitalRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital profile not found"));

        return organRepository.findByHospital(hospital);
    }

    // =========================================================
    // GET MY RECIPIENTS
    // =========================================================
    public List<Recipient> getMyRecipients() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Hospital hospital = hospitalRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital profile not found"));

        return recipientRepository.findByHospital(hospital);
    }

    // =========================================================
    // SEARCH DONOR
    // =========================================================
    public HospitalDonorResponse searchDonorByAadhaar(String aadhaarNumber) {

        if (!aadhaarNumber.matches("\\d{12}"))
            throw new BadRequestException("Invalid Aadhaar number.");

        Donor donor = donorRepository.findByAadhaarNumber(aadhaarNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Donor not found"));

        return HospitalDonorResponse.builder()
                .id(donor.getId())
                .name(donor.getName())
                .email(donor.getEmail())
                .aadhaarNumber(donor.getAadhaarNumber())
                .bloodGroup(donor.getBloodGroup())
                .organsConsented(donor.getOrgansConsented())
                .consentGiven(donor.isConsentGiven())
                .consentHash(donor.getConsentHash())
                .deceased(donor.isDeceased())
                .deathCertificateHash(donor.getDeathCertificateUrl())
                .organsRegistered(donor.isOrgansRegistered())
                .hospitalId(donor.getDeclaredByHospitalId())
                .build();
    }

    // =========================================================
    // GET PROFILE
    // =========================================================
    public Response getHospitalProfile() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Hospital hospital = hospitalRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Complete hospital profile first"));

        HospitalProfileResponse profile = new HospitalProfileResponse();
        profile.setId(hospital.getId());
        profile.setHospitalName(hospital.getHospitalName());
        profile.setRegistrationNumber(hospital.getRegistrationNumber());
        profile.setContactNumber(hospital.getContactNumber());
        profile.setEmail(hospital.getEmail());
        profile.setAddress(hospital.getAddress());
        profile.setApproved(hospital.isApproved());
        profile.setBlocked(hospital.isBlocked());
        profile.setLicenseUrl(hospital.getLicenseUrl());
        profile.setUserID(hospital.getUser().getId());

        Response response = new Response();
        response.setData(profile);
        response.setStatus(Status.SUCCESS);

        return response;
    }
}
