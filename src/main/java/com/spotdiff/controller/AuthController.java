package com.spotdiff.controller;

import com.spotdiff.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired private PlayerService playerService;

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(name = "error", required = false) String error,
                            @RequestParam(name = "logout", required = false) String logout,
                            Model model) {
        if (error != null) model.addAttribute("error", "Identifiants incorrects.");
        if (logout != null) model.addAttribute("logout", "Tu es déconnecté(e).");
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam(name = "username") String username,
                              @RequestParam(name = "password") String password,
                              @RequestParam(name = "confirmPassword") String confirmPassword,
                              RedirectAttributes redirectAttributes) {
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Les mots de passe ne correspondent pas.");
            return "redirect:/register";
        }
        if (password.length() < 4) {
            redirectAttributes.addFlashAttribute("error", "Mot de passe trop court (min 4 caractères).");
            return "redirect:/register";
        }
        try {
            playerService.register(username, password);
            redirectAttributes.addFlashAttribute("success", "Compte créé ! Connecte-toi maintenant.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
}
