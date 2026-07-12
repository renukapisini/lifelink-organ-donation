package com.lifetrace.backend.dto;

import lombok.Data;

@Data
public class DonorRequest {

    private String name;
    private String mobileNumber;
    private String gender;

    private int age;
    private String bloodGroup;
    private String organsConsented;
    private String aadhaarNumber;

}