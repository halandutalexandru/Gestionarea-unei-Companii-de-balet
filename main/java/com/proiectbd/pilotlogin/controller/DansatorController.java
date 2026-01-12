package com.proiectbd.pilotlogin.controller;

import com.proiectbd.pilotlogin.model.Dansator;
import com.proiectbd.pilotlogin.model.Utilizator;
import com.proiectbd.pilotlogin.repository.DansatorRepository;
import com.proiectbd.pilotlogin.service.DansatorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/dansatori")
public class DansatorController {

    @Autowired private DansatorRepository repo;
    @Autowired private DansatorService service;

    private boolean canManage(HttpSession session) {
        Utilizator u = (Utilizator) session.getAttribute("user");
        if (u == null || u.getGrad() == null) return false;
        String g = u.getGrad().trim().toUpperCase();
        return g.equals("ADMIN") || g.equals("ANGAJAT");
    }

    @GetMapping
    public String list(@RequestParam(value="msg", required=false) String msg,
                       @RequestParam(value="err", required=false) String err,
                       HttpSession session,
                       Model model) {
        if (!canManage(session)) return "redirect:/home?err=Nu ai drepturi.";
        model.addAttribute("rows", repo.findAll());
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        return "dansatori";
    }

    @GetMapping("/new")
    public String newForm(HttpSession session, Model model) {
        if (!canManage(session)) return "redirect:/dansatori?err=Nu ai drepturi.";
        model.addAttribute("item", new Dansator());
        model.addAttribute("edit", false);
        return "dansator-form";
    }

    @PostMapping("/new")
    public String create(HttpSession session, @ModelAttribute("item") Dansator item, Model model) {
        if (!canManage(session)) return "redirect:/dansatori?err=Nu ai drepturi.";
        try {
            service.validate(item);
            repo.insert(item);
            return "redirect:/dansatori?msg=Dansator adaugat.";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("edit", false);
            model.addAttribute("err", ex.getMessage());
            return "dansator-form";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(HttpSession session, @PathVariable int id, Model model) {
        if (!canManage(session)) return "redirect:/dansatori?err=Nu ai drepturi.";
        Dansator d = repo.findById(id);
        if (d == null) return "redirect:/dansatori?err=Dansator inexistent.";
        model.addAttribute("item", d);
        model.addAttribute("edit", true);
        return "dansator-form";
    }

    @PostMapping("/edit")
    public String update(HttpSession session, @ModelAttribute("item") Dansator item, Model model) {
        if (!canManage(session)) return "redirect:/dansatori?err=Nu ai drepturi.";
        try {
            service.validate(item);
            repo.update(item);
            return "redirect:/dansatori?msg=Dansator modificat.";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("edit", true);
            model.addAttribute("err", ex.getMessage());
            return "dansator-form";
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(HttpSession session, @PathVariable int id) {
        if (!canManage(session)) return "redirect:/dansatori?err=Nu ai drepturi.";
        try {
            repo.deleteById(id);
            return "redirect:/dansatori?msg=Dansator sters.";
        } catch (DataIntegrityViolationException ex) {
            return "redirect:/dansatori?err=Nu poti sterge: dansatorul este folosit in distributie.";
        }
    }
}
