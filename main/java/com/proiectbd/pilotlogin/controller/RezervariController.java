package com.proiectbd.pilotlogin.controller;

import com.proiectbd.pilotlogin.model.Utilizator;
import com.proiectbd.pilotlogin.repository.RezervariRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RezervariController {

    @Autowired
    private RezervariRepository repo;

    @GetMapping("/rezervarile-mele")
    public String my(HttpSession session, Model model) {
        Utilizator u = (Utilizator) session.getAttribute("user");
        if (u == null || u.getGrad() == null || !u.getGrad().trim().equalsIgnoreCase("CLIENT"))
            return "redirect:/login?msg=Autentifica-te ca CLIENT.";

        model.addAttribute("rows", repo.findMy(u.getId_utilizator()));
        return "rezervarile-mele";
    }
}
