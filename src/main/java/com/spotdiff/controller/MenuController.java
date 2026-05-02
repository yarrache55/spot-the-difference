package com.spotdiff.controller;

import com.spotdiff.model.GameSave;
import com.spotdiff.model.GameSession;
import com.spotdiff.service.GameService;
import com.spotdiff.service.LevelService;
import com.spotdiff.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class MenuController {

    @Autowired private LevelService levelService;
    @Autowired private PlayerService playerService;
    @Autowired private GameService gameService;

    @GetMapping("/menu")
    public String menu(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        playerService.findByUsername(userDetails.getUsername()).ifPresent(p -> {
            model.addAttribute("player", p);
            model.addAttribute("totalLevels", levelService.getTotalLevels());
        });
        return "game/menu";
    }

    @GetMapping("/leaderboard")
    public String leaderboard(Model model) {
        model.addAttribute("players", playerService.getLeaderboard());
        return "game/leaderboard";
    }

    @PostMapping("/game/save")
    public String saveGame(@AuthenticationPrincipal UserDetails userDetails,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        GameSession gameSession = (GameSession) session.getAttribute("gameSession");
        if (gameSession != null) {
            gameService.saveGame(gameSession, userDetails.getUsername());
            redirectAttributes.addFlashAttribute("success", "Partie sauvegardée !");
        }
        return "redirect:/menu";
    }

    @PostMapping("/game/load")
    public String loadGame(@AuthenticationPrincipal UserDetails userDetails,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        Optional<GameSave> saveOpt = gameService.loadLatestSave(userDetails.getUsername());
        if (saveOpt.isPresent()) {
            GameSave save = saveOpt.get();
            GameSession gs = new GameSession();
            gs.reset(save.getLevel());
            gs.setScore(save.getScore());
            gs.setStartTimeMillis(System.currentTimeMillis());
            session.setAttribute("gameSession", gs);
            redirectAttributes.addFlashAttribute("info", "Partie reprise au niveau " + save.getLevel());
            return "redirect:/game/" + save.getLevel();
        }
        redirectAttributes.addFlashAttribute("error", "Aucune sauvegarde trouvée.");
        return "redirect:/menu";
    }
}
