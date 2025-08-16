package com.votingapp.onlinevotingsystem.controller;



import com.votingapp.onlinevotingsystem.entity.Candidate;
import com.votingapp.onlinevotingsystem.entity.Election;
import com.votingapp.onlinevotingsystem.service.ElectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ElectionController {
    private final ElectionService elections;

    public ElectionController(ElectionService elections) { this.elections = elections; }

    // admin-only (secured by SecurityConfig)
    @PostMapping("/admin/elections")
    public ResponseEntity<Election> create(@RequestBody Map<String,String> body) {
        String name = body.get("name");
        LocalDateTime starts = LocalDateTime.parse(body.get("startsAt"));
        LocalDateTime ends = LocalDateTime.parse(body.get("endsAt"));
        return ResponseEntity.ok(elections.create(name, starts, ends));
    }

    @PostMapping("/admin/elections/{id}/candidates")
    public ResponseEntity<Candidate> add(@PathVariable Long id, @RequestBody Map<String,String> body) {
        return ResponseEntity.ok(elections.addCandidate(id, body.get("name"), body.get("party")));
    }

    @PostMapping("/admin/elections/{id}/stop")
    public ResponseEntity<?> stop(@PathVariable Long id) {
        elections.stopEarly(id);
        return ResponseEntity.ok(Map.of("stopped", true));
    }

    @GetMapping("/elections/active")
    public List<Election> active() { return elections.active(); }

    @GetMapping("/elections/{id}")
    public Election get(@PathVariable Long id) { return elections.find(id); }
}
