package com.lifetrace.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;        // RETRIEVED, DISPATCHED, SURGERY_COMPLETED

    private String entityType;    // ORGAN, TRANSPLANT_CASE

    private Long entityId;

    private String oldValue;

    private String newValue;

    private String performedBy;

    private LocalDateTime timestamp;
}
