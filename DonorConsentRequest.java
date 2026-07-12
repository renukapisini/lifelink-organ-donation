package com.lifetrace.backend.dto;

import lombok.Data;

@Data
public class DonorConsentRequest {

    private String consentHash;
    // Blockchain / IPFS hash
}
