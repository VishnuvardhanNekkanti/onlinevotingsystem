package com.votingapp.onlinevotingsystem.entity;



import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="candidates")
public class Candidate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false) private String name;
    @Column(nullable=true) private String party;
    @ManyToOne(optional=false) @JoinColumn(name="election_id")
    @JsonBackReference
    private Election election;
    // getters/setters
}

