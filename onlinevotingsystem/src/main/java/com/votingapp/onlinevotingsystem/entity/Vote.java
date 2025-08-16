package com.votingapp.onlinevotingsystem.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="votes", uniqueConstraints = @UniqueConstraint(columnNames = {"election_id","voter_id"}))
public class Vote {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false) @JoinColumn(name="election_id")
    private Election election;

    @ManyToOne(optional=false) @JoinColumn(name="candidate_id")
    private Candidate candidate;

    @ManyToOne(optional=false) @JoinColumn(name="voter_id")
    private User voter;

    @Column(nullable=false) private LocalDateTime castAt = LocalDateTime.now();
    // getters/setters
}
