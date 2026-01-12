package com.proiectbd.pilotlogin.controller;

import com.proiectbd.pilotlogin.model.Bilet;
import com.proiectbd.pilotlogin.model.Utilizator;
import com.proiectbd.pilotlogin.repository.BiletRepository;
import com.proiectbd.pilotlogin.repository.ReprezentatieRepository;
import com.proiectbd.pilotlogin.service.BiletService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/bilete")
public class BiletController {

    @Autowired private BiletRepository repo;
    @Autowired private BiletService service;
    @Autowired private ReprezentatieRepository repRepo;
    @Autowired
    private ReprezentatieRepository reprezentatieRepo;


    private boolean canManage(HttpSession session) {
        Utilizator u = (Utilizator) session.getAttribute("user");
        if (u == null || u.getGrad() == null) return false;
        String g = u.getGrad().trim().toUpperCase();
        return g.equals("ADMIN") || g.equals("ANGAJAT");
    }

    private List<com.proiectbd.pilotlogin.model.Reprezentatie> repOptions() {
        return repRepo.findAllWithSala();
    }

    @GetMapping
    public String list(@RequestParam(value = "id_reprezentatie", required = false) Integer idReprezentatie,
                       @RequestParam(value = "msg", required = false) String msg,
                       @RequestParam(value = "err", required = false) String err,
                       HttpSession session,
                       Model model) {

        model.addAttribute("rows", repo.findAll(idReprezentatie));
        model.addAttribute("reps", repOptions());
        model.addAttribute("selectedRep", idReprezentatie);
        model.addAttribute("canManage", canManage(session));
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        return "bilete";
    }

    @GetMapping("/new")
    public String newForm(HttpSession session, Model model) {
        if (!canManage(session)) return "redirect:/bilete?err=Nu ai drepturi pentru aceasta actiune.";
        model.addAttribute("item", new Bilet());
        model.addAttribute("reps", reprezentatieRepo.findSpectacoleForBilete());


        model.addAttribute("edit", false);
        return "bilet-form";
    }

    @PostMapping("/new")
    public String create(HttpSession session, @ModelAttribute("item") Bilet item, Model model) {
        if (!canManage(session)) return "redirect:/bilete?err=Nu ai drepturi pentru aceasta actiune.";
        try {
            service.add(item);
            return "redirect:/bilete?msg=Bilet adaugat.";
        } catch (DataIntegrityViolationException ex) {
            // aici intra unique constraint (id_reprezentatie, nr_loc)
            model.addAttribute("reps", repOptions());
            model.addAttribute("edit", false);
            model.addAttribute("err", "Locul este deja folosit pentru aceasta reprezentatie.");
            return "bilet-form";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("reps", repOptions());
            model.addAttribute("edit", false);
            model.addAttribute("err", ex.getMessage());
            return "bilet-form";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(HttpSession session, @PathVariable int id, Model model) {
        if (!canManage(session)) return "redirect:/bilete?err=Nu ai drepturi pentru aceasta actiune.";
        Bilet b = repo.findById(id);
        if (b == null) return "redirect:/bilete?err=Bilet inexistent.";
        model.addAttribute("item", b);
        model.addAttribute("reps", reprezentatieRepo.findSpectacoleForBilete());

        model.addAttribute("edit", true);
        return "bilet-form";
    }

    @PostMapping("/edit")
    public String update(HttpSession session, @ModelAttribute("item") Bilet item, Model model) {
        if (!canManage(session)) return "redirect:/bilete?err=Nu ai drepturi pentru aceasta actiune.";
        try {
            service.edit(item);
            return "redirect:/bilete?msg=Bilet modificat.";
        } catch (DataIntegrityViolationException ex) {
            model.addAttribute("reps", repOptions());
            model.addAttribute("edit", true);
            model.addAttribute("err", "Locul este deja folosit pentru aceasta reprezentatie.");
            return "bilet-form";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("reps", repOptions());
            model.addAttribute("edit", true);
            model.addAttribute("err", ex.getMessage());
            return "bilet-form";
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(HttpSession session, @PathVariable int id) {
        if (!canManage(session)) return "redirect:/bilete?err=Nu ai drepturi pentru aceasta actiune.";
        repo.deleteById(id);
        return "redirect:/bilete?msg=Bilet sters.";
    }
}
