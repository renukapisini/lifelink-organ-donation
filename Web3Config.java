package com.lifetrace.backend.config;

import com.lifetrace.backend.blockchain.LifeTraceRegistryContract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;

@Configuration
public class Web3Config {

    @Value("${blockchain.rpc.url}")
    private String rpcUrl;

    @Value("${blockchain.contract.address}")
    private String contractAddress;

    @Value("${blockchain.private.key}")
    private String privateKey;

    // ✅ ADD THIS
    @Value("${blockchain.chain.id}")
    private long chainId;

    // ===============================
    // Web3 instance
    // ===============================
    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(rpcUrl));
    }

    // ===============================
    // Wallet credentials
    // ===============================
    @Bean
    public Credentials credentials() {
        return Credentials.create(privateKey);
    }

    // ===============================
    // GAS PROVIDER
    // ===============================
    @Bean
    public ContractGasProvider contractGasProvider() {
        return new ContractGasProvider() {

            @Override
            public BigInteger getGasPrice(String contractFunc) {
                return BigInteger.valueOf(30_000_000_000L);
            }

            @Override
            public BigInteger getGasPrice() {
                return BigInteger.valueOf(30_000_000_000L);
            }

            @Override
            public BigInteger getGasLimit(String contractFunc) {
                return BigInteger.valueOf(3_000_000);
            }

            @Override
            public BigInteger getGasLimit() {
                return BigInteger.valueOf(3_000_000);
            }
        };
    }

    // ===============================
    // CONTRACT BEAN (🔥 FIXED)
    // ===============================
    @Bean
    public LifeTraceRegistryContract lifeTraceRegistryContract(
            Web3j web3j,
            Credentials credentials,
            ContractGasProvider gasProvider
    ) {

        // ✅ IMPORTANT FIX
        TransactionManager txManager =
                new RawTransactionManager(web3j, credentials, chainId);

        return LifeTraceRegistryContract.load(
                contractAddress,
                web3j,
                txManager,
                gasProvider
        );
    }
}