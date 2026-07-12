package com.lifetrace.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hospitals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hospitalName;

    private String registrationNumber;

    private String contactNumber;

    @Column(nullable = false)
    private String email;
    private String address;

    private boolean approved;

    private boolean blocked;

    @Column(name = "license_url")
    private String licenseUrl;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}