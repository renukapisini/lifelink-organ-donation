package com.lifetrace.backend.repository;

import com.lifetrace.backend.model.Hospital;
import com.lifetrace.backend.model.Recipient;
import com.lifetrace.backend.util.RecipientStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipientRepository extends JpaRepository<Recipient, Long> {

    List<Recipient> findByHospital(Hospital hospital);

    long countByHospital(Hospital hospital);

    long countByHospitalAndStatus(Hospital hospital, RecipientStatus status);

    long countByStatus(RecipientStatus status);

    List<Recipient> findByOrganTypeAndBloodGroupAndStatus(
            String organType,
            String bloodGroup,
            RecipientStatus status
    );
}
