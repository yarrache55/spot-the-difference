package com.spotdiff.repository;

import com.spotdiff.model.GameSave;
import com.spotdiff.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameSaveRepository extends JpaRepository<GameSave, Long> {
    List<GameSave> findByPlayerOrderBySavedAtDesc(Player player);
    Optional<GameSave> findTopByPlayerOrderBySavedAtDesc(Player player);
    void deleteByPlayer(Player player);
}
