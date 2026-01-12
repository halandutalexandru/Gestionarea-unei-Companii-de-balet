package com.proiectbd.pilotlogin.controller;

import com.proiectbd.pilotlogin.model.Utilizator;
import com.proiectbd.pilotlogin.service.UtilizatorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UtilizatorService utilizatorService;

    @GetMapping("/login")
    public String showLogin(Model model, HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/home";
        }
        model.addAttribute("error", false);
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(
            @RequestParam String username,
            @RequestParam String parola,
            Model model,
            HttpSession session
    ) {
        Utilizator user = utilizatorService.login(username, parola);

        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/home";
        }

        model.addAttribute("error", true);
        return "login";
    }

    @GetMapping("/home")
    public String homePage(HttpSession session, Model model) {
        Utilizator user = (Utilizator) session.getAttribute("user");
        model.addAttribute("user", user);
        return "home";
    }



    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
