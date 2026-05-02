package com.spotdiff.service;

import com.spotdiff.model.Player;
import com.spotdiff.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService implements UserDetailsService {

    @Autowired private PlayerRepository playerRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = playerRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Joueur introuvable: " + username));
        return User.builder()
            .username(player.getUsername())
            .password(player.getPassword())
            .roles("PLAYER")
            .build();
    }

    @Transactional
    public Player register(String username, String rawPassword) {
        if (playerRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Ce pseudo est déjà pris !");
        }
        Player player = new Player(username, passwordEncoder.encode(rawPassword));
        return playerRepository.save(player);
    }

    public Optional<Player> findByUsername(String username) {
        return playerRepository.findByUsername(username);
    }

    @Transactional
    public void updateScore(String username, int newScore, int level) {
        playerRepository.findByUsername(username).ifPresent(player -> {
            player.setTotalScore(player.getTotalScore() + newScore);
            if (newScore > player.getBestScore()) {
                player.setBestScore(newScore);
            }
            if (level > player.getCurrentLevel()) {
                player.setCurrentLevel(level);
            }
            playerRepository.save(player);
        });
    }

    public List<Player> getLeaderboard() {
        return playerRepository.findAllByOrderByBestScoreDesc();
    }
}
