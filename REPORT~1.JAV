package com.lifetrace.backend.controller;

import com.lifetrace.backend.service.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin
@RequiredArgsConstructor
public class ReportingController {

    private final ReportingService reportingService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public Map<String, Object> getAdminStats() {
        return reportingService.getAdminStats();
    }
}