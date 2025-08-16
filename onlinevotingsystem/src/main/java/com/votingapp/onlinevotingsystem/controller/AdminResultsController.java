package com.votingapp.onlinevotingsystem.controller;

import com.votingapp.onlinevotingsystem.entity.Candidate;
import com.votingapp.onlinevotingsystem.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminResultsController {

    private final VoteService votes;

    public AdminResultsController(VoteService votes) {
        this.votes = votes;
    }

    @GetMapping("/elections/{id}/results")
    public ResponseEntity<?> results(@PathVariable Long id) {

        // Get raw results from service
        Map<Candidate, Long> map = votes.results(id);

        // Convert to list of maps with candidate info and votes, then sort by votes descending
        List<Map<String, Object>> list = map.entrySet().stream()
                .map(e -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("candidateId", e.getKey().getId());
                    m.put("name", e.getKey().getName());
                    m.put("party", e.getKey().getParty());
                    m.put("votes", e.getValue());
                    return m;
                })
                .sorted((a, b) -> Long.compare(
                        ((Number)b.get("votes")).longValue(),
                        ((Number)a.get("votes")).longValue()
                ))
                .collect(Collectors.toList());

        // Get the winner (first after sorting)
        Optional<Map<String, Object>> winner = list.stream().findFirst();

        // Build final response
        Map<String, Object> response = new HashMap<>();
        response.put("totalVotes", votes.totalVotes(id));
        response.put("candidates", list);
        response.put("winner", winner.orElse(new HashMap<>()));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/elections/{id}/count")
    public ResponseEntity<?> voteCount(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("totalVotes", votes.totalVotes(id)));
    }
}
