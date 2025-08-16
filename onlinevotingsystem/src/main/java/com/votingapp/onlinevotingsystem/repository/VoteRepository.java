package com.votingapp.onlinevotingsystem.repository;


import com.votingapp.onlinevotingsystem.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByElectionIdAndVoterId(Long electionId, Long voterId);
    long countByElectionIdAndCandidateId(Long electionId, Long candidateId);
    Optional<Vote> findByElectionIdAndVoterId(Long electionId, Long voterId);
    long countByElectionId(Long electionId);
}
