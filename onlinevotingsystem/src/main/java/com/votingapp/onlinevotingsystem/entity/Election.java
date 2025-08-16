package com.votingapp.onlinevotingsystem.entity;



import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data

@Table(name="elections")
public class Election {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false) private String name;
    @Column(nullable=false) private LocalDateTime startsAt;
    @Column(nullable=false) private LocalDateTime endsAt;
    @Column(nullable=false) private boolean stoppedEarly = false;
    @OneToMany(mappedBy="election", cascade=CascadeType.ALL, orphanRemoval=true)
    @JsonManagedReference
    private List<Candidate> candidates;
    // getters/setters
}
