package com.lifetrace.backend.service;

import com.lifetrace.backend.config.JwtUtil;
import com.lifetrace.backend.dto.LoginRequest;
import com.lifetrace.backend.dto.RegisterRequest;
import com.lifetrace.backend.exception.*;
import com.lifetrace.backend.model.*;
import com.lifetrace.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    // ===============================
    // SEND REGISTER OTP
    // ===============================
    public String sendRegisterOtp(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        // 🔥 delete old OTPs
        otpRepository.deleteAll(otpRepository.findAllByEmail(request.getEmail()));

        String otp = generateOtp();

        Otp otpEntity = new Otp();
        otpEntity.setEmail(request.getEmail());
        otpEntity.setOtp(otp);
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(otpEntity);

        emailService.sendOtpEmail(request.getEmail(), otp);

        return "OTP sent";
    }

    // ===============================
    // VERIFY OTP + REGISTER
    // ===============================
    public String verifyRegisterOtp(RegisterRequest request, String otp) {

        Otp storedOtp = otpRepository
                .findTopByEmailOrderByExpiryTimeDesc(request.getEmail())
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (!storedOtp.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        if (storedOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        Role role = roleRepository
                .findByName("ROLE_" + request.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(role));
        user.setVerified(true);

        userRepository.save(user);

        // 🔥 delete used OTP
        otpRepository.delete(storedOtp);

        emailService.sendRegistrationSuccessEmail(user.getEmail());

        return "Registered successfully";
    }

    // ===============================
    // LOGIN
    // ===============================
    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        if (!user.isVerified()) {
            throw new UnauthorizedException("Email not verified");
        }

        emailService.sendLoginAlertEmail(user.getEmail());

        String role = user.getRoles().iterator().next().getName();

        return jwtUtil.generateToken(user.getEmail(), role);
    }

    // ===============================
    // SEND FORGOT OTP
    // ===============================
    public String sendForgotOtp(String email) {

        userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 🔥 delete old OTPs
        otpRepository.deleteAll(otpRepository.findAllByEmail(email));

        String otp = generateOtp();

        Otp otpEntity = new Otp();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(otpEntity);

        emailService.sendOtpEmail(email, otp);

        return "OTP sent";
    }

    // ===============================
    // RESET PASSWORD
    // ===============================
    public String resetPassword(String email, String otp, String newPassword) {

        Otp storedOtp = otpRepository
                .findTopByEmailOrderByExpiryTimeDesc(email)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (!storedOtp.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        if (storedOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // 🔥 delete used OTP
        otpRepository.delete(storedOtp);

        return "Password updated successfully";
    }

    // ===============================
    // GENERATE OTP
    // ===============================
    private String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }
}