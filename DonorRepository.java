package com.lifetrace.backend.repository;

import com.lifetrace.backend.model.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import com.lifetrace.backend.model.User;   // ✅ THIS WAS MISSING


import java.util.List;
import java.util.Optional;

public interface DonorRepository extends JpaRepository<Donor, Long> {

    Optional<Donor> findByUserId(Long userId);
    Optional<Donor> findByUser(User user);

    Donor findByEmail(String email);
    boolean existsByAadhaarNumber(String aadhaarNumber);
    Optional<Donor> findByAadhaarNumber(String aadhaarNumber);


}
