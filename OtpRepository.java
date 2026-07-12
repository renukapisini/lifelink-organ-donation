package com.lifetrace.backend.repository;

import com.lifetrace.backend.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {

    // 🔥 Get latest OTP
    Optional<Otp> findTopByEmailOrderByExpiryTimeDesc(String email);

    // 🔥 Get all OTPs (for cleanup)
    List<Otp> findAllByEmail(String email);
}