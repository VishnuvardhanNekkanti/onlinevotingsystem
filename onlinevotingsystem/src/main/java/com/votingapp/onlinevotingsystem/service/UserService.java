package com.votingapp.onlinevotingsystem.service;



import com.votingapp.onlinevotingsystem.entity.User;
import com.votingapp.onlinevotingsystem.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository users;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository users) { this.users = users; }

    public User register(String email, String rawPassword) {
        if (users.existsByEmail(email)) throw new IllegalArgumentException("Email in use");
        User u = new User();
        u.setEmail(email);
        u.setPasswordHash(encoder.encode(rawPassword));
        u.setRole(User.Role.VOTER);
        return users.save(u);
    }

    public User authenticate(String email, String rawPassword) {
        User u = users.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid creds"));
        if (!encoder.matches(rawPassword, u.getPasswordHash())) throw new IllegalArgumentException("Invalid creds");
        return u;
    }
}
