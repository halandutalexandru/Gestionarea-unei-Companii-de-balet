package com.proiectbd.pilotlogin.controller;

import com.proiectbd.pilotlogin.model.Utilizator;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {

    @ModelAttribute
    public void addUser(HttpSession session, Model model) {
        Utilizator user = (Utilizator) session.getAttribute("user");
        model.addAttribute("user", user);

        boolean isAdmin = (user != null) && "ADMIN".equalsIgnoreCase(user.getGrad()); // sau getRol()
        model.addAttribute("isAdmin", isAdmin);
    }
}
