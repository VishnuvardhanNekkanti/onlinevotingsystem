package com.votingapp.onlinevotingsystem.service;

import com.votingapp.onlinevotingsystem.entity.*;
import com.votingapp.onlinevotingsystem.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VoteService {
    private final VoteRepository votes;
    private final ElectionRepository elections;
    private final CandidateRepository candidates;
    private final UserRepository users;

    public VoteService(VoteRepository votes, ElectionRepository elections,
                       CandidateRepository candidates, UserRepository users) {
        this.votes = votes;
        this.elections = elections;
        this.candidates = candidates;
        this.users = users;
    }

    @Transactional
    public Vote castVote(Long electionId, Long candidateId, Long voterId) {
        Election e = elections.findById(electionId)
                .orElseThrow(() -> new IllegalArgumentException("No election"));
        LocalDateTime now = LocalDateTime.now();
        if (e.isStoppedEarly() || now.isBefore(e.getStartsAt()) || now.isAfter(e.getEndsAt()))
            throw new IllegalStateException("Election not active");

        Candidate c = candidates.findById(candidateId).orElseThrow();
        if (!c.getElection().getId().equals(electionId))
            throw new IllegalArgumentException("Candidate not in election");

        if (votes.existsByElectionIdAndVoterId(electionId, voterId))
            throw new IllegalStateException("Already voted");

        Vote v = new Vote();
        v.setElection(e);
        v.setCandidate(c);
        v.setVoter(users.findById(voterId).orElseThrow());
        return votes.save(v);
    }

    // NEW: Expose repository method to check if user has already voted
    public Optional<Vote> findByElectionIdAndVoterId(Long electionId, Long voterId) {
        return votes.findByElectionIdAndVoterId(electionId, voterId);
    }

    public Map<Candidate, Long> results(Long electionId) {
        List<Candidate> list = candidates.findByElectionId(electionId);
        return list.stream()
                .collect(Collectors.toMap(c -> c,
                        c -> votes.countByElectionIdAndCandidateId(electionId, c.getId())));
    }

    public long totalVotes(Long electionId) {
        return votes.countByElectionId(electionId);
    }
}
