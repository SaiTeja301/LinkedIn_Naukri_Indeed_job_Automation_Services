package com.jobbot.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String encryptedPassword;

    private Integer experienceYears;

    @Column(columnDefinition = "TEXT")
    private String preferredRoles; // Stored as JSON string

    @Column(columnDefinition = "TEXT")
    private String preferredCompanies; // Stored as JSON string

    private boolean remote;
    private boolean hybrid;
}
