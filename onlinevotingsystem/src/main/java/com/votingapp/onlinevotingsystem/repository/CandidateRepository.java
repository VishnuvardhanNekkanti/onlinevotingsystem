package com.votingapp.onlinevotingsystem.repository;

//package com.example.votingapp.repo;

//import com.example.votingapp.domain.Candidate;
import com.votingapp.onlinevotingsystem.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    List<Candidate> findByElectionId(Long electionId);
}
