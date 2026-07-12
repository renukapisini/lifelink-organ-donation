package com.lifetrace.backend.blockchain;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager; // ✅ ADDED
import org.web3j.tx.gas.ContractGasProvider;

public class LifeTraceRegistryContract extends Contract {

    public static final String BINARY = "0x";

    // ✅ UPDATED CONSTRUCTOR (ONLY CHANGE)
    protected LifeTraceRegistryContract(
            String contractAddress,
            Web3j web3j,
            TransactionManager transactionManager,
            ContractGasProvider gasProvider
    ) {
        super(BINARY, contractAddress, web3j, transactionManager, gasProvider);
    }

    @Override
    protected String resolveContractAddress(String contractAddress) {
        return contractAddress;
    }

    // ✅ UPDATED LOAD METHOD (ONLY CHANGE)
    public static LifeTraceRegistryContract load(
            String contractAddress,
            Web3j web3j,
            TransactionManager txManager,
            ContractGasProvider gasProvider
    ) {
        return new LifeTraceRegistryContract(
                contractAddress,
                web3j,
                txManager,
                gasProvider
        );
    }

    // ============================================================
    // DONOR CONSENT
    // ============================================================

    public TransactionReceipt storeDonorConsent(
            BigInteger donorId,
            String ipfsHash
    ) throws Exception {

        return executeRemoteCallTransaction(
                new org.web3j.abi.datatypes.Function(
                        "storeDonorConsent",
                        Arrays.asList(
                                new Uint256(donorId),
                                new Utf8String(ipfsHash)
                        ),
                        Collections.emptyList()
                )
        ).send();
    }

    public List<Type> getDonorConsent(BigInteger donorId) throws Exception {

        return executeCallMultipleValueReturn(
                new org.web3j.abi.datatypes.Function(
                        "getDonorConsent",
                        Arrays.asList(new Uint256(donorId)),
                        Arrays.asList(
                                new TypeReference<Utf8String>() {},
                                new TypeReference<Bool>() {},
                                new TypeReference<Uint256>() {}
                        )
                )
        );
    }

    // ============================================================
    // ORGAN ALLOCATION
    // ============================================================

    public TransactionReceipt storeOrganAllocation(
            BigInteger organId,
            BigInteger donorId,
            BigInteger hospitalId
    ) throws Exception {

        return executeRemoteCallTransaction(
                new org.web3j.abi.datatypes.Function(
                        "storeOrganAllocation",
                        Arrays.asList(
                                new Uint256(organId),
                                new Uint256(donorId),
                                new Uint256(hospitalId)
                        ),
                        Collections.emptyList()
                )
        ).send();
    }

    public List<Type> getOrganAllocation(BigInteger organId) throws Exception {

        return executeCallMultipleValueReturn(
                new org.web3j.abi.datatypes.Function(
                        "getOrganAllocation",
                        Arrays.asList(new Uint256(organId)),
                        Arrays.asList(
                                new TypeReference<Uint256>() {},
                                new TypeReference<Uint256>() {},
                                new TypeReference<Uint256>() {},
                                new TypeReference<Uint256>() {}
                        )
                )
        );
    }

    // ============================================================
    // SURGERY RESULT
    // ============================================================

    public TransactionReceipt storeSurgeryResult(
            BigInteger caseId,
            boolean success
    ) throws Exception {

        return executeRemoteCallTransaction(
                new org.web3j.abi.datatypes.Function(
                        "storeSurgeryResult",
                        Arrays.asList(
                                new Uint256(caseId),
                                new Bool(success)
                        ),
                        Collections.emptyList()
                )
        ).send();
    }

    public List<Type> getSurgeryResult(BigInteger caseId) throws Exception {

        return executeCallMultipleValueReturn(
                new org.web3j.abi.datatypes.Function(
                        "getSurgeryResult",
                        Arrays.asList(new Uint256(caseId)),
                        Arrays.asList(
                                new TypeReference<Uint256>() {},
                                new TypeReference<Bool>() {},
                                new TypeReference<Uint256>() {}
                        )
                )
        );
    }
}