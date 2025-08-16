package com.votingapp.onlinevotingsystem.controller;



import com.votingapp.onlinevotingsystem.entity.User;
import com.votingapp.onlinevotingsystem.security.JwtService;
import com.votingapp.onlinevotingsystem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService users;
    private final JwtService jwt;

    public AuthController(UserService users, JwtService jwt) { this.users = users; this.jwt = jwt; }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        String password = body.get("password");
        User u = users.register(email, password);
        String token = jwt.generateToken(u.getEmail(), u.getRole().name());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        String password = body.get("password");
        User u = users.authenticate(email, password);
        String token = jwt.generateToken(u.getEmail(), u.getRole().name());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
