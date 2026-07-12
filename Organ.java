package com.lifetrace.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lifetrace.backend.util.OrganStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "organ")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Organ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String organType;

    private String bloodGroup;

    @Column(name = "organ_condition")
    private String condition;

    @Enumerated(EnumType.STRING)
    private OrganStatus status;

    private Long donorId;

    private String location;

    @Column(name = "blockchain_tx_hash")
    private String blockchainTxHash;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    @JsonIgnore
    private Recipient recipient;

    @ManyToOne
    @JoinColumn(name = "hospital_id", nullable = false)
    @JsonIgnore
    private Hospital hospital;
}
