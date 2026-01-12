package com.proiectbd.pilotlogin.controller;

import com.proiectbd.pilotlogin.model.Reprezentatie;
import com.proiectbd.pilotlogin.model.Utilizator;
import com.proiectbd.pilotlogin.repository.ReprezentatieRepository;
import com.proiectbd.pilotlogin.repository.SalaRepository;
import com.proiectbd.pilotlogin.service.ReprezentatieService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reprezentatii")
public class ReprezentatieController {

    @Autowired private ReprezentatieRepository repo;
    @Autowired private SalaRepository salaRepo;
    @Autowired private ReprezentatieService service;

    private boolean canManage(HttpSession session) {
        Utilizator u = (Utilizator) session.getAttribute("user");
        if (u == null || u.getGrad() == null) return false;
        String g = u.getGrad().trim().toUpperCase();
        return g.equals("ADMIN") || g.equals("ANGAJAT");
    }

    @GetMapping
    public String list(HttpSession session, Model model,
                       @RequestParam(value = "msg", required = false) String msg,
                       @RequestParam(value = "err", required = false) String err) {
        model.addAttribute("rows", repo.findAllWithSala());
        model.addAttribute("canManage", canManage(session));
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        return "reprezentatii";
    }

    @GetMapping("/new")
    public String newForm(HttpSession session, Model model) {
        if (!canManage(session)) return "redirect:/reprezentatii?err=Nu ai drepturi pentru aceasta actiune.";
        model.addAttribute("item", new Reprezentatie());
        model.addAttribute("sali", salaRepo.findAll());
        model.addAttribute("edit", false);
        return "reprezentatie-form";
    }

    @PostMapping("/new")
    public String create(HttpSession session, @ModelAttribute("item") Reprezentatie item, Model model) {
        if (!canManage(session)) return "redirect:/reprezentatii?err=Nu ai drepturi pentru aceasta actiune.";
        try {
            service.add(item);
            return "redirect:/reprezentatii?msg=Adaugat cu succes.";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("sali", salaRepo.findAll());
            model.addAttribute("edit", false);
            model.addAttribute("err", ex.getMessage());
            return "reprezentatie-form";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(HttpSession session, @PathVariable int id, Model model) {
        if (!canManage(session)) return "redirect:/reprezentatii?err=Nu ai drepturi pentru aceasta actiune.";
        Reprezentatie r = repo.findById(id);
        if (r == null) return "redirect:/reprezentatii?err=Reprezentatia nu exista.";
        model.addAttribute("item", r);
        model.addAttribute("sali", salaRepo.findAll());
        model.addAttribute("edit", true);
        return "reprezentatie-form";
    }

    @PostMapping("/edit")
    public String update(HttpSession session, @ModelAttribute("item") Reprezentatie item, Model model) {
        if (!canManage(session)) return "redirect:/reprezentatii?err=Nu ai drepturi pentru aceasta actiune.";
        try {
            service.edit(item);
            return "redirect:/reprezentatii?msg=Modificat cu succes.";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("sali", salaRepo.findAll());
            model.addAttribute("edit", true);
            model.addAttribute("err", ex.getMessage());
            return "reprezentatie-form";
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(HttpSession session, @PathVariable int id) {
        if (!canManage(session)) return "redirect:/reprezentatii?err=Nu ai drepturi pentru aceasta actiune.";
        try {
            repo.deleteById(id);
            return "redirect:/reprezentatii?msg=sters cu succes.";
        } catch (DataIntegrityViolationException ex) {
            // apare daca exista Bilete legate prin FK
            return "redirect:/reprezentatii?err=Nu poti sterge: exista bilete asociate.";
        }
    }
}
