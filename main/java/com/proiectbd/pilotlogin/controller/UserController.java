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

import java.util.List;

@Controller
public class UserController {

    private static final List<String> ROLES = List.of("ADMIN", "ANGAJAT", "CLIENT", "ARTIST");

    @Autowired
    private UtilizatorService utilizatorService;

    private boolean isAdmin(HttpSession session) {
        Utilizator u = (Utilizator) session.getAttribute("user");
        return u != null && u.getGrad() != null && u.getGrad().trim().equalsIgnoreCase("ADMIN");
    }


    @GetMapping("/add-user")
    public String showAddUserForm(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/home";
        }
        model.addAttribute("roles", ROLES);
        model.addAttribute("errorMsg", null);
        return "add-user";
    }

    @PostMapping("/add-user")
    public String addUser(
            @RequestParam String username,
            @RequestParam String parola,
            @RequestParam String grad,
            @RequestParam(required = false) Integer id_dansator,
            HttpSession session,
            Model model
    ) {
        if (!isAdmin(session)) {
            return "redirect:/home";
        }

        try {
            utilizatorService.addUser(username, parola, grad, id_dansator);
            return "redirect:/home";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("roles", ROLES);
            model.addAttribute("errorMsg", ex.getMessage());
            return "add-user";
        }
    }
}
