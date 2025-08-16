package com.votingapp.onlinevotingsystem.repository;
import com.votingapp.onlinevotingsystem.entity.Election;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ElectionRepository extends JpaRepository<Election, Long> {
    List<Election> findByStartsAtBeforeAndEndsAtAfterAndStoppedEarlyFalse(LocalDateTime before, LocalDateTime after);
}
