package com.lifetrace.backend.repository;

import com.lifetrace.backend.model.Hospital;
import com.lifetrace.backend.model.Organ;
import com.lifetrace.backend.util.OrganStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrganRepository extends JpaRepository<Organ, Long> {

    List<Organ> findByHospital(Hospital hospital);

    long countByHospital(Hospital hospital);

    long countByHospitalAndStatus(Hospital hospital, OrganStatus status);

    long countByStatus(OrganStatus status);

    List<Organ> findByOrganTypeAndBloodGroupAndStatus(
            String organType,
            String bloodGroup,
            OrganStatus status
    );
}
