package com.lifetrace.backend.dto;

import com.lifetrace.backend.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.Setter;

@Setter
@Data
public class HospitalProfileResponse {

    private Long id;
    private String hospitalName;
    private String registrationNumber;
    private String contactNumber;
    private String email;
    private String address;
    private boolean approved;
    private boolean blocked;
    private String licenseUrl;
    private long userID;
}


