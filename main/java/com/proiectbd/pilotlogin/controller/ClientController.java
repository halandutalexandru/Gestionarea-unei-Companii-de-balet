package com.proiectbd.pilotlogin.controller;

import com.proiectbd.pilotlogin.model.Utilizator;
import com.proiectbd.pilotlogin.repository.ClientRepository;
import com.proiectbd.pilotlogin.service.RezervareService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ClientController {

    @Autowired private ClientRepository repo;
    @Autowired private RezervareService rezervareService;

    private Utilizator requireClient(HttpSession session) {
        Utilizator u = (Utilizator) session.getAttribute("user");
        if (u == null || u.getGrad() == null) return null;
        if (!u.getGrad().trim().equalsIgnoreCase("CLIENT")) return null;
        return u;
    }

    @GetMapping("/spectacole")
    public String spectacole(HttpSession session, Model model) {
        if (requireClient(session) == null) return "redirect:/login?msg=Autentifica-te ca CLIENT.";
        model.addAttribute("rows", repo.spectacole());
        return "spectacole";
    }

    @GetMapping("/spectacole/{id}/bilete")
    public String bilete(@PathVariable int id, HttpSession session,
                         @RequestParam(value="msg", required=false) String msg,
                         @RequestParam(value="err", required=false) String err,
                         Model model) {
        if (requireClient(session) == null) return "redirect:/login?msg=Autentifica-te ca CLIENT.";
        model.addAttribute("rows", repo.bileteByReprezentatie(id));

        model.addAttribute("idRep", id);
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        return "bilete-client";
    }

    @PostMapping("/rezerva/{idBilet}")
    public String rezerva(@PathVariable int idBilet,
                          @RequestParam int idRep,
                          HttpSession session) {
        Utilizator u = requireClient(session);
        if (u == null) return "redirect:/login?msg=Autentifica-te ca CLIENT.";

        boolean ok = rezervareService.rezerva(idBilet, u.getId_utilizator());
        if (!ok) return "redirect:/spectacole/" + idRep + "/bilete?err=Bilet indisponibil (deja rezervat/vandut).";
        return "redirect:/spectacole/" + idRep + "/bilete?msg=Rezervare efectuata.";
    }
}
