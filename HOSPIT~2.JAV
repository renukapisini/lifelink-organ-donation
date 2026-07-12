package com.lifetrace.backend.controller;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HospitalDonorResponse {

    private Long id;
    private String name;
    private String email;
    private String aadhaarNumber;
    private String bloodGroup;
    private String organsConsented;
    private boolean consentGiven;
    private String consentHash;
    private boolean deceased;
    private String deathCertificateHash;
    private boolean organsRegistered;
    private Long hospitalId;

}
