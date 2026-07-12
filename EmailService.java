package com.lifetrace.backend.service;

import com.lifetrace.backend.model.Organ;
import com.lifetrace.backend.model.Recipient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${BREVO_API_KEY}")
    private String API_KEY;

    private void sendEmail(String to, String subject, String html) {
        try {
            String url = "https://api.brevo.com/v3/smtp/email";

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("accept", "application/json");
            headers.set("api-key", API_KEY);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = new HashMap<>();

            Map<String, String> sender = new HashMap<>();
            sender.put("email", "harshalmulay1039@gmail.com"); // MUST be verified
            sender.put("name", "LifeTrace");

            Map<String, String> toMap = new HashMap<>();
            toMap.put("email", to);

            body.put("sender", sender);
            body.put("to", new Object[]{toMap});
            body.put("subject", subject);
            body.put("htmlContent", html);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            restTemplate.postForEntity(url, request, String.class);

            System.out.println("Email sent to: " + to);

        } catch (Exception e) {
            System.out.println("Email failed: " + to);
            e.printStackTrace();
        }
    }

    // ================= OTP =================
    @Async
    public void sendOtpEmail(String email, String otp) {
        sendEmail(email,
                "LifeTrace - OTP",
                "<h2>Your OTP: " + otp + "</h2>");
    }

    // ================= MATCH =================
    @Async
    public void sendHospitalMatchEmail(String email, Organ organ, Recipient recipient) {
        sendEmail(email,
                "Organ Match Found",
                "<p>Organ: " + organ.getOrganType() + "</p>" +
                        "<p>Blood: " + organ.getBloodGroup() + "</p>" +
                        "<p>Recipient ID: " + recipient.getId() + "</p>");
    }

    @Async
    public void sendDispatchEmail(String email, Long caseId) {
        sendEmail(email,
                "Organ Dispatched",
                "<p>Case ID: " + caseId + "</p>");
    }

    @Async
    public void sendReceiveEmail(String email, Long caseId) {
        sendEmail(email,
                "Organ Received",
                "<p>Case ID: " + caseId + "</p>");
    }

    @Async
    public void sendSurgeryResultEmail(String email, Long caseId, boolean success) {
        sendEmail(email,
                "Surgery Result",
                "<p>Case ID: " + caseId + "</p>" +
                        "<p>Status: " + (success ? "SUCCESS" : "FAILED") + "</p>");
    }

    @Async
    public void sendRegistrationSuccessEmail(String email) {
        sendEmail(email,
                "Registration Successful",
                "<p>Your account created successfully.</p>");
    }

    @Async
    public void sendLoginAlertEmail(String email) {
        sendEmail(email,
                "Login Alert",
                "<p>You logged in successfully.</p>");
    }

    @Async
    public void sendHospitalStatusEmail(String email, String status) {
        sendEmail(email,
                "Hospital Status",
                "<p>Status: " + status + "</p>");
    }
}