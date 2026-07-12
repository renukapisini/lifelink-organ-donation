package com.lifetrace.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "donors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Donor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ===============================
    // NEW FIELDS
    // ===============================
    private String name;

    private String mobileNumber;

    private String gender;

    private String email;

    @Column(unique = true, nullable = false)
    private String aadhaarNumber;

    // ===============================
    // EXISTING FIELDS
    // ===============================
    private String bloodGroup;

    private int age;

    private String organsConsented;

    @Column(length = 1000)
    private String consentHash;   // IPFS hash

    private boolean consentGiven;

    @Column(unique = true)
    private Long blockchainDonorId;


    private Long declaredByHospitalId;

    @Column(name = "death_certificate_url")
    private String deathCertificateUrl;

    private boolean deceased;
    private boolean organsRegistered;

}