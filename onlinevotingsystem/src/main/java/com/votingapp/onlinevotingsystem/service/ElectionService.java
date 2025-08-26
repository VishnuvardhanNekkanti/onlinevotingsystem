package com.votingapp.onlinevotingsystem.service;



import com.votingapp.onlinevotingsystem.entity.Candidate;
import com.votingapp.onlinevotingsystem.entity.Election;
import com.votingapp.onlinevotingsystem.repository.CandidateRepository;
import com.votingapp.onlinevotingsystem.repository.ElectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ElectionService {
    private final ElectionRepository elections;
    private final CandidateRepository candidates;

    public ElectionService(ElectionRepository elections, CandidateRepository candidates) {
        this.elections = elections; this.candidates = candidates;
    }

    public Election create(String name, LocalDateTime starts, LocalDateTime ends) {
        if (!ends.isAfter(starts)) throw new IllegalArgumentException("endsAt must be after startsAt");
        Election e = new Election();
        e.setName(name);
        e.setStartsAt(starts);
        e.setEndsAt(ends);
        return elections.save(e);
    }

    public Candidate addCandidate(Long electionId, String name, String party) {
        Election e = elections.findById(electionId).orElseThrow();
        Candidate c = new Candidate();
        c.setName(name);
        c.setParty(party);
        c.setElection(e);
        return candidates.save(c);
    }

    public List<Election> active() {
        LocalDateTime now = LocalDateTime.now();
        return elections.findByStartsAtBeforeAndEndsAtAfterAndStoppedEarlyFalse(now, now);
    }

    public Election find(Long id) { return elections.findById(id).orElseThrow(); }

    @Transactional
    public Election stopEarly(Long id) {
        Election e = find(id);
        e.setStoppedEarly(true);
        return elections.save(e);
    }

    public List<Candidate> getCandidatesForElection(Long electionId) {
        return candidates.findByElectionId(electionId);
    }

    public List<Election> findAll() {
        return elections.findAll();
    }
}
