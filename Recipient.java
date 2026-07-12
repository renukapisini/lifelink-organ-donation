package com.lifetrace.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lifetrace.backend.util.RecipientStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recipients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recipient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String organType;

    private String bloodGroup;

    @Column(nullable = false)
    private Integer age;

    private String gender;

    private String location;

    private String urgencyLevel;

    @Enumerated(EnumType.STRING)
    private RecipientStatus status;

    @ManyToOne
    @JoinColumn(name = "hospital_id", nullable = false)
    @JsonIgnore
    private Hospital hospital;
}
