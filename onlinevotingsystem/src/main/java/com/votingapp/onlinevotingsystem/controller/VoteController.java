package com.votingapp.onlinevotingsystem.controller;



import com.votingapp.onlinevotingsystem.entity.Vote;
import com.votingapp.onlinevotingsystem.service.VoteService;
import com.votingapp.onlinevotingsystem.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/vote")
public class VoteController {
    private final VoteService votes;
    private final UserRepository users;

    public VoteController(VoteService votes, UserRepository users) { this.votes = votes; this.users = users; }

    @PostMapping("/{electionId}")
    public ResponseEntity<?> cast(@PathVariable Long electionId, @RequestBody Map<String,Long> body, Authentication auth) {
        Long candidateId = body.get("candidateId");
        String email = auth.getName();
        Long voterId = users.findByEmail(email).orElseThrow().getId();

        // Backend enforces unique vote — front end should show modal before calling this API
        Vote v = votes.castVote(electionId, candidateId, voterId);
        return ResponseEntity.ok(Map.of("voteId", v.getId()));
    }

    @GetMapping("/{electionId}/status")
    public ResponseEntity<?> status(@PathVariable Long electionId, Authentication auth) {
        String email = auth.getName();
        Long voterId = users.findByEmail(email).orElseThrow().getId();
        var optional = votes.findByElectionIdAndVoterId(electionId, voterId);
        return ResponseEntity.ok(optional.map(v -> Map.of("voted", true, "candidateId", v.getCandidate().getId()))
                .orElse(Map.of("voted", false)));
    }
}
