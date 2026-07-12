package com.lifetrace.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class RegisterOrganRequest {

    private Long donorId;

    private List<OrganRequestItem> organs;



}