package com.lifetrace.backend.controller;

import com.lifetrace.backend.dto.RegisterOrganRequest;
import com.lifetrace.backend.model.Organ;
import com.lifetrace.backend.service.OrganService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organs")
@RequiredArgsConstructor
@CrossOrigin
public class OrganController {

    private final OrganService organService;

    // ===============================
    // REGISTER ORGAN (AUTO MATCH)
    // ===============================
//    @PostMapping("/register")
//    public ResponseEntity<Organ> registerOrgan(
//            @RequestBody RegisterOrganRequest request
//    ) {
//        return ResponseEntity.ok(
//                organService.registerOrgan(request)
//        );
//    }
}