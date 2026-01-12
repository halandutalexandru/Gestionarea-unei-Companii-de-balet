package com.proiectbd.pilotlogin.controller;

import com.proiectbd.pilotlogin.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {

    @Autowired
    private AuthRepository repo;

    @GetMapping("/register")
    public String form(@RequestParam(value="err", required=false) String err,
                       @RequestParam(value="msg", required=false) String msg,
                       Model model) {
        model.addAttribute("err", err);
        model.addAttribute("msg", msg);
        return "register";
    }

    @PostMapping("/register")
    public String submit(@RequestParam String username,
                         @RequestParam String password) {

        String u = username == null ? "" : username.trim();
        String p = password == null ? "" : password.trim();

        if (u.isEmpty() || p.isEmpty()) return "redirect:/register?err=Completeaza username si parola.";
        if (u.length() < 3) return "redirect:/register?err=Username prea scurt.";
        if (p.length() < 3) return "redirect:/register?err=Parola prea scurta.";

        try {
            repo.registerClient(u, p);
            return "redirect:/login?msg=Cont creat. Te poti autentifica.";
        } catch (DuplicateKeyException ex) {
            return "redirect:/register?err=Username deja exista.";
        }
    }
}
