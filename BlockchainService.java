package com.lifetrace.backend.service;

import com.lifetrace.backend.blockchain.LifeTraceRegistryContract;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockchainService {

    private final LifeTraceRegistryContract contract;

    // ============================================================
    // STORE DONOR CONSENT
    // ============================================================

    public TransactionReceipt storeDonorConsent(
            Long donorId,
            String ipfsHash
    ) {
        try {
            return contract.storeDonorConsent(
                    BigInteger.valueOf(donorId),
                    ipfsHash
            );
        } catch (Exception e) {
            throw new RuntimeException("Blockchain donor consent failed", e);
        }
    }

    // ============================================================
    // READ DONOR CONSENT  ✅ REQUIRED BY BlockchainAuditService
    // ============================================================

    public String getDonorConsentHash(Long donorId) {
        try {
            List<Type> result = contract.getDonorConsent(
                    BigInteger.valueOf(donorId)
            );

            if (result == null || result.isEmpty()) return null;

            return result.get(0).getValue().toString();

        } catch (Exception e) {
            throw new RuntimeException("Blockchain read failed", e);
        }
    }

    // ============================================================
    // STORE ORGAN ALLOCATION
    // ============================================================

    public TransactionReceipt storeOrganAllocation(
            Long organId,
            Long donorId,
            Long hospitalId
    ) {
        try {
            return contract.storeOrganAllocation(
                    BigInteger.valueOf(organId),
                    BigInteger.valueOf(donorId),
                    BigInteger.valueOf(hospitalId)
            );
        } catch (Exception e) {
            throw new RuntimeException("Blockchain allocation failed", e);
        }
    }

    // ============================================================
    // READ ORGAN ALLOCATION  ✅ REQUIRED BY BlockchainAuditService
    // ============================================================

    public List<Type> getOrganAllocation(Long organId) {
        try {
            return contract.getOrganAllocation(
                    BigInteger.valueOf(organId)
            );
        } catch (Exception e) {
            throw new RuntimeException("Blockchain allocation read failed", e);
        }
    }

    // ============================================================
    // STORE SURGERY RESULT
    // ============================================================

    public TransactionReceipt storeSurgeryResult(
            Long caseId,
            boolean success
    ) {
        try {
            return contract.storeSurgeryResult(
                    BigInteger.valueOf(caseId),
                    success
            );
        } catch (Exception e) {
            throw new RuntimeException("Blockchain surgery record failed", e);
        }
    }

    // ============================================================
    // READ SURGERY RESULT
    // ============================================================

    public List<Type> getSurgeryResult(Long caseId) {
        try {
            return contract.getSurgeryResult(
                    BigInteger.valueOf(caseId)
            );
        } catch (Exception e) {
            throw new RuntimeException("Blockchain surgery read failed", e);
        }
    }
}
