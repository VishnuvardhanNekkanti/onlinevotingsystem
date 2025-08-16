package com.votingapp.onlinevotingsystem.entity;

import jakarta.persistence.*;
import jdk.jfr.DataAmount;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=150)
    private String email;

    @Column(nullable=false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=16)
    private Role role = Role.VOTER;

    @Column(nullable=false)
    private boolean enabled = true;

    @Column(nullable=false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Role { ADMIN, VOTER }

    // getters & setters (or Lombok)
}
