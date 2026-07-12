package com.lifetrace.backend.controller;

import com.lifetrace.backend.dto.LoginRequest;
import com.lifetrace.backend.dto.RegisterRequest;
import com.lifetrace.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ===============================
    // REGISTER - STEP 1 (SEND OTP)
    // ===============================
    @PostMapping("/register-otp")
    public ResponseEntity<?> sendRegisterOtp(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.sendRegisterOtp(request));
    }

    // ===============================
    // REGISTER - STEP 2 (VERIFY OTP + CREATE USER)
    // ===============================
    @PostMapping("/register-verify")
    public ResponseEntity<?> verifyRegisterOtp(
            @RequestBody RegisterRequest request,
            @RequestParam String otp
    ) {
        return ResponseEntity.ok(authService.verifyRegisterOtp(request, otp));
    }

    // ===============================
    // LOGIN
    // ===============================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // ===============================
    // FORGOT PASSWORD - SEND OTP
    // ===============================
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        return ResponseEntity.ok(authService.sendForgotOtp(email));
    }

    // ===============================
    // RESET PASSWORD
    // ===============================
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword
    ) {
        return ResponseEntity.ok(
                authService.resetPassword(email, otp, newPassword)
        );
    }
}