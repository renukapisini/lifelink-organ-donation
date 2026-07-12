package com.lifetrace.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "blockchain_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockchainTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String txHash;

    private String action;
    // DONOR_CONSENT, ORGAN_ALLOCATION

    private Long donorId;

    private Long organId;

    private LocalDateTime timestamp;
}
