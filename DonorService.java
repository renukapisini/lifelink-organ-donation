package com.lifetrace.backend.service;

import com.lifetrace.backend.dto.DonorRequest;
import com.lifetrace.backend.dto.Response;
import com.lifetrace.backend.exception.BadRequestException;
import com.lifetrace.backend.exception.ResourceNotFoundException;
import com.lifetrace.backend.exception.UnauthorizedException;
import com.lifetrace.backend.model.Donor;
import com.lifetrace.backend.model.User;
import com.lifetrace.backend.repository.DonorRepository;
import com.lifetrace.backend.repository.UserRepository;
import com.lifetrace.backend.util.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DonorService {

    private final DonorRepository donorRepository;
    private final UserRepository userRepository;
    private final IpfsService ipfsService;
    private final BlockchainService blockchainService;

    // ============================================================
    // CREATE DONOR PROFILE
    // ============================================================
    @Transactional
    public Donor createDonorProfile(DonorRequest request) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        if (!request.getAadhaarNumber().matches("\\d{12}")) {
            throw new BadRequestException("Invalid Aadhaar number. Must be 12 digits.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (donorRepository.findByUser(user).isPresent()) {
            throw new BadRequestException("Donor profile already exists");
        }

        if (donorRepository.existsByAadhaarNumber(request.getAadhaarNumber())) {
            throw new BadRequestException("Aadhaar already registered");
        }


        Donor donor = new Donor();
        donor.setUser(user);

        // NEW FIELDS
        donor.setName(request.getName());
        donor.setMobileNumber(request.getMobileNumber());
        donor.setGender(request.getGender());
        donor.setEmail(user.getEmail());
        donor.setAadhaarNumber(request.getAadhaarNumber());


        // EXISTING FIELDS
        donor.setAge(request.getAge());
        donor.setBloodGroup(request.getBloodGroup());
        donor.setOrgansConsented(request.getOrgansConsented());
        donor.setConsentGiven(false);
        donor.setDeceased(false);

        Donor savedDonor = donorRepository.save(donor);

        savedDonor.setBlockchainDonorId(savedDonor.getId());

        return donorRepository.save(savedDonor);
    }

    // ============================================================
    // UPLOAD CONSENT
    // ============================================================
    @Transactional
    public String uploadConsent(Long donorId, MultipartFile file) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Donor not found with ID: " + donorId));

        // Ownership validation
        if (!donor.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You are not allowed to upload consent for this donor");
        }

        if (donor.isConsentGiven()) {
            throw new BadRequestException("Consent already uploaded");
        }

        // 1️⃣ Upload to IPFS
        String ipfsHash = ipfsService.uploadFile(file);

        // 2️⃣ Store on blockchain
        blockchainService.storeDonorConsent(
                donor.getBlockchainDonorId(),
                ipfsHash
        );

        // 3️⃣ Update DB
        donor.setConsentHash(ipfsHash);
        donor.setConsentGiven(true);

        donorRepository.save(donor);

        return ipfsHash;
    }


    public Response getDonorProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        System.out.println(email);
        Donor donor=donorRepository.findByEmail(email);
        Response response = new Response();
        if (email==null || donor==null) {
            response.setData("complete the donor profile first");
            response.setStatus(Status.FAIL);
        }else {
            response.setData(donor);
            response.setStatus(Status.SUCCESS);
        }
        return  response;
    }


}